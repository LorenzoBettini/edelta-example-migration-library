package edelta;

import edelta.lib.EdeltaDefaultRuntime;
import edelta.lib.EdeltaEcoreUtil;
import edelta.lib.EdeltaEngine;
import edelta.lib.EdeltaModelMigrator;
import edelta.lib.EdeltaRuntime;
import edelta.lib.annotation.EdeltaGenerated;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class FromV3ToV4 extends EdeltaDefaultRuntime {
  public FromV3ToV4(final EdeltaRuntime other) {
    super(other);
  }

  public void compactItems(final EPackage it) {
    final Consumer<EAttribute> _function = (EAttribute it_1) -> {
      it_1.setDefaultValue(Integer.valueOf(1));
    };
    this.stdLib.addNewEAttribute(getEClass("library", "BookItem"), "numOfCopies", getEDataType("ecore", "EInt"), _function);
    final EReference bookItemsFeature = getEReference("library", "Library", "bookItems");
    final EReference bookFeature = getEReference("library", "BookItem", "book");
    final EAttribute numOfCopiesFeature = getEAttribute("library", "BookItem", "numOfCopies");
    final Consumer<EdeltaModelMigrator> _function_1 = (EdeltaModelMigrator it_1) -> {
      final EdeltaModelMigrator.CopyProcedure _function_2 = (EStructuralFeature oldBookItemsFeature, EObject oldLibrary, EObject newLibrary) -> {
        final List<EObject> origElements = EdeltaEcoreUtil.getValueAsList(oldLibrary, oldBookItemsFeature);
        final Function<EObject, EObject> _function_3 = (EObject oldBookItem) -> {
          return EdeltaEcoreUtil.getValueAsEObject(oldBookItem, it_1.<EReference>getOriginal(bookFeature));
        };
        final Supplier<LinkedHashMap<EObject, List<EObject>>> _function_4 = () -> {
          return new LinkedHashMap<EObject, List<EObject>>();
        };
        final LinkedHashMap<EObject, List<EObject>> groupedByBook = origElements.stream().collect(
          Collectors.groupingBy(_function_3, _function_4, 
            Collectors.<EObject>toList()));
        final Function<Map.Entry<EObject, List<EObject>>, EObject> _function_5 = (Map.Entry<EObject, List<EObject>> entry) -> {
          EObject _xblockexpression = null;
          {
            EObject oldBookItem = IterableExtensions.<EObject>head(entry.getValue());
            final Consumer<EObject> _function_6 = (EObject it_2) -> {
              it_2.eSet(numOfCopiesFeature, Integer.valueOf(entry.getValue().size()));
            };
            _xblockexpression = it_1.createFrom(it_1.<EClass>getMigrated(oldBookItem.eClass()), oldBookItem, _function_6);
          }
          return _xblockexpression;
        };
        final List<EObject> newBookItems = groupedByBook.entrySet().stream().<EObject>map(_function_5).toList();
        newLibrary.eSet(bookItemsFeature, newBookItems);
      };
      it_1.copyRule(
        it_1.<EStructuralFeature>wasRelatedTo(bookItemsFeature), _function_2);
    };
    this.modelMigration(_function_1);
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
