package edelta.example.migration.library.migrator;

import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource;

import books.BooksPackage;
import edelta.FromV1ToV2;
import edelta.FromV2ToV3;
import edelta.FromV3ToV4;
import edelta.lib.EdeltaVersionMigrator;
import library.LibraryPackage;

public class LibraryModelMigrator {

	public Collection<Resource> execute(String modelPath) throws Exception {
		var edeltaMigrator = new EdeltaVersionMigrator();
		// load the current (latest) version of EPackages
		edeltaMigrator.loadCurrentEPackages(LibraryPackage.eINSTANCE, BooksPackage.eINSTANCE);
		// register the Edelta migration code
		edeltaMigrator.registerMigration(FromV1ToV2::new);
		edeltaMigrator.registerMigration(FromV2ToV3::new);
		edeltaMigrator.registerMigration(FromV3ToV4::new);
		// load the models to check and migrate
		edeltaMigrator.addModelFileExtensions(".library", ".books");
		edeltaMigrator.loadModelsFrom(modelPath);
		// let the Edelta framework do the rest
		return edeltaMigrator.execute();
	}
}
