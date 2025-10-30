package edelta.example.migration.library.performance;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

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

		// Size series: total number of Book elements across the two .books files, doubling up to 8192
		// Each .books file will contain half of TOTAL_BOOKS (so min per DB is 2)
		static final int SIZE_TOTAL_BOOKS_START = 4;   // matches the templates: 2+2 books
		static final int SIZE_TOTAL_BOOKS_MAX = 8192;
		static final int SIZE_FACTOR = 2;

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
			System.out.println("Base templates: " + Config.TEMPLATES_DIR);
			System.out.println("Generated inputs under: " + Config.PERF_BASE_DIR.toAbsolutePath());

			warmUp();

			var filesResults = measureByNumberOfFiles();
			var sizeResults = measureByModelSize();

			System.out.println();
			System.out.println("-- Trend: Number of files vs time --");
			printResults("files", filesResults);

			System.out.println();
			System.out.println("-- Trend: Model size (total Book elements) vs time --");
			printResults("totalBooks", sizeResults);
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
			for (int totalBooks = Config.SIZE_TOTAL_BOOKS_START;
				 totalBooks <= Config.SIZE_TOTAL_BOOKS_MAX;
				 totalBooks *= Config.SIZE_FACTOR) {
				int perDb = Math.max(2, totalBooks / 2); // split evenly across the two DBs
				cleanDirectory(seriesDir);
				generateFixedFourFiles(seriesDir, perDb);
				long best = Long.MAX_VALUE;
				for (int it = 0; it < Config.ITERATIONS_PER_POINT; it++) {
					// Re-generate to ensure models are at v1 for each iteration
					cleanDirectory(seriesDir);
					generateFixedFourFiles(seriesDir, perDb);
					long t0 = System.nanoTime();
					migrator.execute(seriesDir.toString());
					long elapsed = System.nanoTime() - t0;
					best = Math.min(best, elapsed);
				}
				results.put(totalBooks, best);
				System.out.println("  totalBooks=" + totalBooks + " (per DB=" + perDb + ") -> " + formatNanos(best));
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

		private void generateFixedFourFiles(Path dir, int booksPerDB) throws IOException {
			// Use the original names (no suffix) while ensuring references match
			writeBooks(dir.resolve(Config.DB1_BASE + ".books"), booksPerDB);
			writeBooks(dir.resolve(Config.DB2_BASE + ".books"), booksPerDB);
			writeLibrary(dir.resolve(Config.LIB1_BASE + ".library"),
				Config.DB1_BASE + ".books", 0,
				Config.DB2_BASE + ".books", 0);
			writeLibrary(dir.resolve(Config.LIB2_BASE + ".library"),
				Config.DB1_BASE + ".books", 1,
				Config.DB2_BASE + ".books", 1);
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
			// Prefer the Edelta test utils if available; otherwise, do a simple recursive delete
			try {
				EdeltaTestUtils.cleanDirectoryRecursive(dir.toString());
				return;
			} catch (Throwable t) {
				// fallback
			}
			if (Files.exists(dir)) {
				Files.walk(dir)
					.sorted(Comparator.reverseOrder())
					.forEach(p -> {
						try { Files.deleteIfExists(p); } catch (Exception ignore) {}
					});
			}
			Files.createDirectories(dir);
		}
	}
}