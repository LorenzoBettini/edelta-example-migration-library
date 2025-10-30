package edelta.example.migration.library.example;

import edelta.example.migration.library.migrator.LibraryModelMigrator;
import edelta.testutils.EdeltaTestUtils;

/**
 * A simple example showing how to use the LibraryModelMigrator
 * to migrate library models from an input directory to an output directory.
 * 
 * @author Lorenzo Bettini
 */
public class LibraryModelMigratorExample {

	/**
	 * Main method to run the migration example.
	 * 
	 * @param args command line arguments; the first argument can be the base directory
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Accept base directory as first argument, default to current directory
		String baseDir = args.length > 0 ? args[0] : ".";
		String inputDir = baseDir + "/inputs/v1";
		String outputDir = baseDir + "/output/";
		LibraryModelMigrator migrator = new LibraryModelMigrator();
		// Copy input files to output directory
		EdeltaTestUtils.cleanDirectoryRecursive(outputDir);
		EdeltaTestUtils.copyDirectory(inputDir, outputDir);
		// Apply migration
		migrator.execute(outputDir);
		System.out.println("Migration completed successfully.");
	}
}
