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

	public static void main(String[] args) throws Exception {
		String inputDir = "inputs/v1";
		String outputDir = "output/";
		LibraryModelMigrator migrator = new LibraryModelMigrator();
		// Copy input files to output directory
		EdeltaTestUtils.cleanDirectoryRecursive(outputDir);
		EdeltaTestUtils.copyDirectory(inputDir, outputDir);
		// Apply migration
		migrator.execute(outputDir);
		System.out.println("Migration completed successfully.");
	}
}
