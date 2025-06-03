package edelta;

import edelta.lib.EdeltaDefaultRuntime;
import edelta.lib.EdeltaEngine;
import edelta.lib.EdeltaRuntime;
import edelta.lib.annotation.EdeltaGenerated;
import edelta.refactorings.lib.EdeltaRefactorings;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;

@SuppressWarnings("all")
public class FromV2ToV3 extends EdeltaDefaultRuntime {
  private EdeltaRefactorings refactorings;

  public FromV2ToV3(final EdeltaRuntime other) {
    super(other);
    refactorings = new EdeltaRefactorings(this);
  }

  public void applyReferenceToClass(final EPackage it) {
    this.refactorings.referenceToClass(
      "BookItem", 
      getEReference("library", "Library", "books"));
    getEReference("library", "Library", "books").setName("bookItems");
    getEReference("books", "Book", "libraries").setName("libraryBookItems");
  }

  @Override
  public void performSanityChecks() throws Exception {
    ensureEPackageIsLoadedByNsURI("library", "http://edelta/Library/v2");
    ensureEPackageIsLoadedByNsURI("books", "http://edelta/Books/v2");
  }

  @Override
  protected void doExecute() throws Exception {
    applyReferenceToClass(getEPackage("library"));
    getEPackage("library").setNsURI("http://edelta/Library/v3");
    getEPackage("books").setNsURI("http://edelta/Books/v3");
  }

  @Override
  public List<String> getMigratedNsURIs() {
    return List.of(
      "http://edelta/Library/v2",
      "http://edelta/Books/v2"
    );
  }

  @Override
  public List<String> getMigratedEcorePaths() {
    return List.of(
      "/library/v2/Library.ecore",
      "/library/v2/Books.ecore"
    );
  }

  @EdeltaGenerated
  public static void main(final String[] args) throws Exception {
    var engine = new EdeltaEngine(FromV2ToV3::new);
    engine.loadEcoreFile("Library.ecore",
      FromV2ToV3.class.getResourceAsStream("/library/v2/Library.ecore"));
    engine.loadEcoreFile("Books.ecore",
      FromV2ToV3.class.getResourceAsStream("/library/v2/Books.ecore"));
    engine.execute();
    engine.save("modified");
  }
}
