package edelta.example.migration.library.performance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edelta.example.migration.library.migrator.LibraryModelMigrator;
import edelta.testutils.EdeltaTestUtils;

public class EdeltaLibraryModelMigratorPerformanceStatistics {
	public static void main(String[] args) throws Exception {
		// Silence INFO logging for this execution only, keep WARNING and ERROR
		Logger.getRootLogger().setLevel(Level.WARN);
		new Runner().runAll();
	}

	/**
	 * Encapsulates configuration knobs for the performance measurements so they can be
	 * easily tweaked in one place.
	 */
	static final class Config {
		// Base template location (relative to the tests module basedir)
		// This is not used directly in the current implementation, but kept for reference:
		// all inputs are generated programmatically, taking inspiration from these templates.
		static final String TEMPLATES_DIR = "inputs/v1";

		// Where to generate inputs (cleaned before each run)
		static final Path PERF_BASE_DIR = Paths.get("target", "performance-inputs");

		// Warm-up iterations before real measurements
		static final int WARMUP_ITERATIONS = 1;

		// How many iterations per measurement point (kept 1 as requested, but tweakable)
		static final int ITERATIONS_PER_POINT = 1;

		// Number-of-files series: from 4 to 1024 doubling
		static final int FILES_START = 4;
		static final int FILES_MAX = 1024;
		static final int FILES_FACTOR = 2;

		// Size series: duplication factor for template content, doubling up to max
		// Factor 1 = original templates, Factor 2 = duplicate all elements once, etc.
		static final int SIZE_FACTOR_START = 1;   // original template size
		static final int SIZE_FACTOR_MAX = 2048;  // 2048x duplication gives ~8192 elements
		static final int SIZE_MULTIPLY = 2;

		// Fixed names used by the templates
		static final String DB1_BASE = "MyBookDatabase1";
		static final String DB2_BASE = "MyBookDatabase2";
		static final String LIB1_BASE = "MyLibrary1";
		static final String LIB2_BASE = "MyLibrary2";
	}

	/**
	 * Orchestrates the two measurement series and prints results.
	 */
	static final class Runner {
		private final LibraryModelMigrator migrator =
				new LibraryModelMigrator();

		void runAll() throws Exception {
			System.out.println("=== Edelta LibraryModelMigrator Performance ===");
			System.out.println("Generated inputs under: " + Config.PERF_BASE_DIR.toAbsolutePath());

			warmUp();

			var filesResults = measureByNumberOfFiles();
			var sizeResults = measureByModelSize();

			System.out.println();
			System.out.println("-- Trend: Number of files vs time --");
			printResults("files", filesResults);

			System.out.println();
			System.out.println("-- Trend: Model size (total elements) vs time --");
			printResults("totalElements", sizeResults);
		}

		private void warmUp() throws Exception {
			System.out.println();
			System.out.println("Warming up JVM (" + Config.WARMUP_ITERATIONS + " iteration(s))...");
			var warmDir = Config.PERF_BASE_DIR.resolve("warmup");
			for (int i = 0; i < Config.WARMUP_ITERATIONS; i++) {
				cleanDirectory(warmDir);
				// 1 group -> 4 files, minimal size (2 books per DB)
				generateFileGroup(warmDir, "warm", 1, /*booksPerDB*/2);
				long t0 = System.nanoTime();
				migrator.execute(warmDir.toString());
				long elapsed = System.nanoTime() - t0;
				System.out.println("  warmup #" + (i + 1) + ": " + formatNanos(elapsed));
			}
		}

		private LinkedHashMap<Integer, Long> measureByNumberOfFiles() throws Exception {
			System.out.println();
			System.out.println("Measuring impact of number of files (constant size per file)...");
			var seriesDir = Config.PERF_BASE_DIR.resolve("files");
			var results = new LinkedHashMap<Integer, Long>();
			for (int files = Config.FILES_START; files <= Config.FILES_MAX; files *= Config.FILES_FACTOR) {
				int groups = Math.max(1, files / 4); // 4 files per group
				cleanDirectory(seriesDir);
				// Keep per-file size at template's size (2 books per DB)
				for (int g = 1; g <= groups; g++) {
					generateFileGroup(seriesDir, "g" + g, g, /*booksPerDB*/2);
				}
				long best = Long.MAX_VALUE;
				for (int it = 0; it < Config.ITERATIONS_PER_POINT; it++) {
					// Re-generate to ensure models are at v1 for each iteration
					cleanDirectory(seriesDir);
					for (int g = 1; g <= groups; g++) {
						generateFileGroup(seriesDir, "g" + g, g, /*booksPerDB*/2);
					}
					long t0 = System.nanoTime();
					migrator.execute(seriesDir.toString());
					long elapsed = System.nanoTime() - t0;
					best = Math.min(best, elapsed);
				}
				results.put(files, best);
				System.out.println("  files=" + files + " -> " + formatNanos(best));
			}
			return results;
		}

		private LinkedHashMap<Integer, Long> measureByModelSize() throws Exception {
			System.out.println();
			System.out.println("Measuring impact of model size (constant number of files = 4)...");
			var seriesDir = Config.PERF_BASE_DIR.resolve("size");
			var results = new LinkedHashMap<Integer, Long>();
			for (int factor = Config.SIZE_FACTOR_START;
				 factor <= Config.SIZE_FACTOR_MAX;
				 factor *= Config.SIZE_MULTIPLY) {
				try {
					cleanDirectory(seriesDir);
					duplicateTemplatesWithFactor(seriesDir, factor);
					// Count elements BEFORE migration (after generation)
					int totalElements = countTotalElements(seriesDir);
					
					if (totalElements == 0) {
						System.err.println("  WARNING: factor=" + factor + " resulted in 0 elements. Skipping.");
						continue;
					}
					
					long best = Long.MAX_VALUE;
					for (int it = 0; it < Config.ITERATIONS_PER_POINT; it++) {
						// Re-generate to ensure models are at v1 for each iteration
						cleanDirectory(seriesDir);
						duplicateTemplatesWithFactor(seriesDir, factor);
						long t0 = System.nanoTime();
						migrator.execute(seriesDir.toString());
						long elapsed = System.nanoTime() - t0;
						best = Math.min(best, elapsed);
					}
					
					results.put(totalElements, best);
					System.out.println("  factor=" + factor + ", totalElements=" + totalElements + " -> " + formatNanos(best));
				} catch (Exception e) {
					System.err.println("  ERROR at factor=" + factor + ": " + e.getMessage());
					e.printStackTrace();
					throw e;
				}
			}
			return results;
		}

		private void printResults(String xLabel, Map<Integer, Long> data) {
			System.out.println(xLabel + ",time_ms");
			data.forEach((x, nanos) -> System.out.println(x + "," + (nanos / 1_000_000.0)));
		}

		private static String formatNanos(long nanos) {
			double ms = nanos / 1_000_000.0;
			if (ms < 1000) return String.format(Locale.ROOT, "%.2f ms", ms);
			return String.format(Locale.ROOT, "%.3f s", ms / 1000.0);
		}

		/**
		 * Generate files with duplicated content by the given factor. Factor 1 = original template size.
		 * This duplicates all elements in both .books and .library files programmatically.
		 */
		private void duplicateTemplatesWithFactor(Path dir, int factor) throws IOException {
			// Generate .books files with duplicated book elements
			// Template DB1 has: "first book", "second book"
			writeBooksWithDuplication(dir.resolve(Config.DB1_BASE + ".books"), 
					new String[]{"first book", "second book"}, factor);
			// Template DB2 has: "another book", "yet another book"
			writeBooksWithDuplication(dir.resolve(Config.DB2_BASE + ".books"), 
					new String[]{"another book", "yet another book"}, factor);
			
			// Generate .library files with duplicated book references
			// Template LIB1 references: DB1[0], DB2[0]
			writeLibraryWithDuplication(dir.resolve(Config.LIB1_BASE + ".library"),
					Config.DB1_BASE + ".books", new int[]{0},
					Config.DB2_BASE + ".books", new int[]{0}, factor);
			// Template LIB2 references: DB1[1], DB2[1]
			writeLibraryWithDuplication(dir.resolve(Config.LIB2_BASE + ".library"),
					Config.DB1_BASE + ".books", new int[]{1},
					Config.DB2_BASE + ".books", new int[]{1}, factor);
		}

		/**
		 * Generate a .books file with duplicated book elements.
		 */
		private static void writeBooksWithDuplication(Path path, String[] bookTitles, int factor) throws IOException {
			Files.createDirectories(path.getParent());
			try (var out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				out.write("<books:BookDatabase\n");
				out.write("    xmi:version=\"2.0\"\n");
				out.write("    xmlns:xmi=\"http://www.omg.org/XMI\"\n");
				out.write("    xmlns:books=\"http://edelta/Books/v1\">\n");
				
				// Duplicate all book elements by the factor
				for (int f = 0; f < factor; f++) {
					for (String title : bookTitles) {
						out.write("  <books title=\"" + title + "\"/>\n");
					}
				}
				
				out.write("</books:BookDatabase>\n");
			}
		}

		/**
		 * Generate a .library file with duplicated book references.
		 * Indices are updated to reference the duplicated books correctly.
		 */
		private static void writeLibraryWithDuplication(Path path,
				String db1File, int[] db1Indices,
				String db2File, int[] db2Indices, int factor) throws IOException {
			Files.createDirectories(path.getParent());
			try (var out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				out.write("<library:Library\n");
				out.write("    xmi:version=\"2.0\"\n");
				out.write("    xmlns:xmi=\"http://www.omg.org/XMI\"\n");
				out.write("    xmlns:library=\"http://edelta/Library/v1\">\n");
				
				// Duplicate all references by the factor, updating indices
				int totalRefsPerIteration = db1Indices.length + db2Indices.length;
				for (int f = 0; f < factor; f++) {
					for (int idx : db1Indices) {
						int newIndex = idx + (f * totalRefsPerIteration);
						out.write("  <books href=\"" + db1File + "#//@books." + newIndex + "\"/>\n");
					}
					for (int idx : db2Indices) {
						int newIndex = idx + (f * totalRefsPerIteration);
						out.write("  <books href=\"" + db2File + "#//@books." + newIndex + "\"/>\n");
					}
				}
				
				out.write("</library:Library>\n");
			}
		}

		/**
		 * Count total model elements (books + library references) for reporting.
		 */
		private int countTotalElements(Path dir) throws IOException {
			int total = 0;
			Pattern bookPattern = Pattern.compile("\\s*<books\\s+title=\"[^\"]+\"\\s*/>");
			Pattern refPattern = Pattern.compile("\\s*<books\\s+href=\"[^\"]+\"\\s*/>");
			
			for (String fileName : new String[]{
					Config.DB1_BASE + ".books", Config.DB2_BASE + ".books",
					Config.LIB1_BASE + ".library", Config.LIB2_BASE + ".library"}) {
				Path file = dir.resolve(fileName);
				if (Files.exists(file)) {
					List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
					for (String line : lines) {
						if (bookPattern.matcher(line).matches() || refPattern.matcher(line).matches()) {
							total++;
						}
					}
				}
			}
			return total;
		}

		private void generateFileGroup(Path dir, String tag, int groupIndex, int booksPerDB) throws IOException {
			// Produce 4 files with unique names per group and consistent cross-refs
			String db1 = Config.DB1_BASE + "-" + groupIndex + ".books";
			String db2 = Config.DB2_BASE + "-" + groupIndex + ".books";
			String lib1 = Config.LIB1_BASE + "-" + groupIndex + ".library";
			String lib2 = Config.LIB2_BASE + "-" + groupIndex + ".library";
			writeBooks(dir.resolve(db1), booksPerDB);
			writeBooks(dir.resolve(db2), booksPerDB);
			writeLibrary(dir.resolve(lib1), db1, 0, db2, 0);
			writeLibrary(dir.resolve(lib2), db1, 1, db2, 1);
		}

		private static void writeBooks(Path path, int booksPerDB) throws IOException {
			Files.createDirectories(path.getParent());
			try (var out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				out.write("<books:BookDatabase\n");
				out.write("    xmi:version=\"2.0\"\n");
				out.write("    xmlns:xmi=\"http://www.omg.org/XMI\"\n");
				out.write("    xmlns:books=\"http://edelta/Books/v1\">\n");
				for (int i = 0; i < booksPerDB; i++) {
					out.write("  <books title=\"book " + (i + 1) + "\"/>\n");
				}
				out.write("</books:BookDatabase>\n");
			}
		}

		private static void writeLibrary(Path path,
										 String db1File, int db1Index,
								 String db2File, int db2Index) throws IOException {
			Files.createDirectories(path.getParent());
			try (var out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				out.write("<library:Library\n");
				out.write("    xmi:version=\"2.0\"\n");
				out.write("    xmlns:xmi=\"http://www.omg.org/XMI\"\n");
				out.write("    xmlns:library=\"http://edelta/Library/v1\">\n");
				out.write("  <books href=\"" + db1File + "#//@books." + db1Index + "\"/>\n");
				out.write("  <books href=\"" + db2File + "#//@books." + db2Index + "\"/>\n");
				out.write("</library:Library>\n");
			}
		}

		private static void cleanDirectory(Path dir) throws IOException {
			EdeltaTestUtils.cleanDirectoryRecursive(dir.toString());
		}
	}
}