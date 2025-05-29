/**
 */
package library.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import books.Book;
import library.BookItem;
import library.Library;
import library.LibraryPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see library.LibraryPackage
 * @generated
 */
public class LibraryValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final LibraryValidator INSTANCE = new LibraryValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "library";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LibraryValidator() {
		super();
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return LibraryPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case LibraryPackage.LIBRARY:
				return validateLibrary((Library)value, diagnostics, context);
			case LibraryPackage.BOOK_ITEM:
				return validateBookItem((BookItem)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLibrary(Library library, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(library, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(library, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(library, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(library, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(library, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(library, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(library, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(library, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(library, diagnostics, context);
		if (result || diagnostics != null) result &= validateLibrary_noDuplicateBooks(library, diagnostics, context);
		return result;
	}

	/**
	 * Validates the noDuplicateBooks constraint of '<em>Library</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateLibrary_noDuplicateBooks(Library library, DiagnosticChain diagnostics, Map<Object, Object> context) {
		Map<Book, List<BookItem>> bookToItems = new HashMap<>();
		boolean valid = true;

		for (BookItem item : library.getBookItems()) {
			Book book = item.getBook();
			if (book != null) {
				bookToItems.computeIfAbsent(book, b -> new ArrayList<>()).add(item);
			}
		}

		for (Map.Entry<Book, List<BookItem>> entry : bookToItems.entrySet()) {
			List<BookItem> items = entry.getValue();
			if (items.size() > 1) {
				valid = false;
				if (diagnostics != null) {
					diagnostics.add(new BasicDiagnostic(
						Diagnostic.ERROR,
						DIAGNOSTIC_SOURCE,
						0,
						"Duplicate BookItem refer to Book '" + entry.getKey().getTitle() + "'",
						items.toArray()));
				}
			}
		}

		return valid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBookItem(BookItem bookItem, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(bookItem, diagnostics, context);
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} //LibraryValidator
