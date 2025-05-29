package edelta;

import edelta.lib.EdeltaDefaultRuntime;
import edelta.lib.EdeltaEngine;
import edelta.lib.EdeltaRuntime;
import edelta.lib.annotation.EdeltaGenerated;
import edelta.refactorings.lib.EdeltaRefactorings;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EPackage;

@SuppressWarnings("all")
public class FromV3ToV4 extends EdeltaDefaultRuntime {
  private EdeltaRefactorings refactorings;

  public FromV3ToV4(final EdeltaRuntime other) {
    super(other);
    refactorings = new EdeltaRefactorings(this);
  }

  public void compactItems(final EPackage it) {
    final Consumer<EAttribute> _function = (EAttribute it_1) -> {
      it_1.setDefaultValue(Integer.valueOf(1));
    };
    this.stdLib.addNewEAttribute(getEClass("library", "BookItem"), "numOfCopies", getEDataType("ecore", "EInt"), _function);
  }

  @Override
  public void performSanityChecks() throws Exception {
    ensureEPackageIsLoaded("ecore");
    ensureEPackageIsLoadedByNsURI("library", "http://edelta/Library/v3");
    ensureEPackageIsLoadedByNsURI("books", "http://edelta/Books/v3");
  }

  @Override
  protected void doExecute() throws Exception {
    compactItems(getEPackage("library"));
    getEPackage("library").setNsURI("http://edelta/Library/v4");
    getEPackage("books").setNsURI("http://edelta/Books/v4");
  }

  @Override
  public List<String> getMigratedNsURIs() {
    return List.of(
      "http://edelta/Library/v3",
      "http://edelta/Books/v3"
    );
  }

  @Override
  public List<String> getMigratedEcorePaths() {
    return List.of(
      "/v3/Library.ecore",
      "/v3/Books.ecore"
    );
  }

  @EdeltaGenerated
  public static void main(final String[] args) throws Exception {
    var engine = new EdeltaEngine(FromV3ToV4::new);
    engine.loadEcoreFile("Library.ecore",
      FromV3ToV4.class.getResourceAsStream("/v3/Library.ecore"));
    engine.loadEcoreFile("Books.ecore",
      FromV3ToV4.class.getResourceAsStream("/v3/Books.ecore"));
    engine.execute();
    engine.save("modified");
  }
}
