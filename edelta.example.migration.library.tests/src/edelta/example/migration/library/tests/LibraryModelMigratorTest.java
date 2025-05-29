package edelta.example.migration.library.tests;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;
import org.junit.Test;

import edelta.example.migration.library.migrator.LibraryModelMigrator;
import edelta.testutils.EdeltaTestUtils;

public class LibraryModelMigratorTest {

	private static final String MY_LIBRARY2_LIBRARY = "MyLibrary2.library";
	private static final String MY_LIBRARY1_LIBRARY = "MyLibrary1.library";
	private static final String MY_BOOK_DATABASE2_BOOKS = "MyBookDatabase2.books";
	private static final String MY_BOOK_DATABASE1_BOOKS = "MyBookDatabase1.books";
	private static final String OUTPUT = "output/";
	private static final String INPUTS = "inputs/";
	private String expectationDir = "expectations/";

	private LibraryModelMigrator migrator;

	@Before
	public void setup() throws IOException {
		expectationDir = "expectations/";
		EdeltaTestUtils.cleanDirectoryRecursive(OUTPUT);
		migrator = new LibraryModelMigrator();
	}

	@Test
	public void testFromV1() throws Exception {
		EdeltaTestUtils.copyDirectory(INPUTS + "v1", OUTPUT);
		applyMigrationAndAssertCorrectness();
	}

	@Test
	public void testFromV2() throws Exception {
		EdeltaTestUtils.copyDirectory(INPUTS + "v2", OUTPUT);
		applyMigrationAndAssertCorrectness();
	}

	private void applyMigrationAndAssertCorrectness() throws Exception {
		Collection<Resource> migrated = migrator.execute(OUTPUT);
		EdeltaTestUtils.assertFilesAreEquals(
				expectationDir + MY_BOOK_DATABASE1_BOOKS,
				OUTPUT + MY_BOOK_DATABASE1_BOOKS);
		EdeltaTestUtils.assertFilesAreEquals(
				expectationDir + MY_BOOK_DATABASE2_BOOKS,
				OUTPUT + MY_BOOK_DATABASE2_BOOKS);
		EdeltaTestUtils.assertFilesAreEquals(
				expectationDir + MY_LIBRARY1_LIBRARY,
				OUTPUT + MY_LIBRARY1_LIBRARY);
		EdeltaTestUtils.assertFilesAreEquals(
				expectationDir + MY_LIBRARY2_LIBRARY,
				OUTPUT + MY_LIBRARY2_LIBRARY);
		EdeltaTestUtils.assertResourcesAreValid(migrated);
	}
}
