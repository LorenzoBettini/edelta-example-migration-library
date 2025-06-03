package edelta.exaple.migration.library.migrator;

import edelta.lib.EdeltaDefaultRuntime;
import edelta.lib.EdeltaEngine;
import edelta.lib.EdeltaRuntime;
import edelta.lib.EdeltaUtils;
import edelta.lib.annotation.EdeltaGenerated;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class FromV1ToV2 extends EdeltaDefaultRuntime {
  public FromV1ToV2(final EdeltaRuntime other) {
    super(other);
  }

  public void introduceOpposites(final EPackage it) {
    final Procedure1<EClass> _function = (EClass it_1) -> {
      final Consumer<EReference> _function_1 = (EReference it_2) -> {
        EdeltaUtils.makeBidirectional(it_2, getEReference("books", "BookDatabase", "books"));
      };
      this.stdLib.addNewEReference(it_1, "database", getEClass("books", "BookDatabase"), _function_1);
      final Consumer<EReference> _function_2 = (EReference it_2) -> {
        EdeltaUtils.makeMultiple(it_2);
      };
      this.stdLib.addNewEReference(it_1, "libraries", getEClass("library", "Library"), _function_2);
    };
    ObjectExtensions.<EClass>operator_doubleArrow(
      getEClass("books", "Book"), _function);
    EdeltaUtils.makeBidirectional(getEReference("library", "Library", "books"), getEReference("books", "Book", "libraries"));
  }

  @Override
  public void performSanityChecks() throws Exception {
    ensureEPackageIsLoadedByNsURI("library", "http://edelta/Library/v1");
    ensureEPackageIsLoadedByNsURI("books", "http://edelta/Books/v1");
  }

  @Override
  protected void doExecute() throws Exception {
    introduceOpposites(getEPackage("books"));
    getEPackage("library").setNsURI("http://edelta/Library/v2");
    getEPackage("books").setNsURI("http://edelta/Books/v2");
  }

  @Override
  public List<String> getMigratedNsURIs() {
    return List.of(
      "http://edelta/Library/v1",
      "http://edelta/Books/v1"
    );
  }

  @Override
  public List<String> getMigratedEcorePaths() {
    return List.of(
      "/library/v1/Library.ecore",
      "/library/v1/Books.ecore"
    );
  }

  @EdeltaGenerated
  public static void main(final String[] args) throws Exception {
    var engine = new EdeltaEngine(FromV1ToV2::new);
    engine.loadEcoreFile("Library.ecore",
      FromV1ToV2.class.getResourceAsStream("/library/v1/Library.ecore"));
    engine.loadEcoreFile("Books.ecore",
      FromV1ToV2.class.getResourceAsStream("/library/v1/Books.ecore"));
    engine.execute();
    engine.save("modified");
  }
}
