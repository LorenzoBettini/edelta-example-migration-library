/**
 */
package library;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Library</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link library.Library#getBookItems <em>Book Items</em>}</li>
 * </ul>
 *
 * @see library.LibraryPackage#getLibrary()
 * @model
 * @generated
 */
public interface Library extends EObject {
	/**
	 * Returns the value of the '<em><b>Book Items</b></em>' containment reference list.
	 * The list contents are of type {@link library.BookItem}.
	 * It is bidirectional and its opposite is '{@link library.BookItem#getLibrary <em>Library</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Book Items</em>' containment reference list.
	 * @see library.LibraryPackage#getLibrary_BookItems()
	 * @see library.BookItem#getLibrary
	 * @model opposite="library" containment="true"
	 * @generated
	 */
	EList<BookItem> getBookItems();

} // Library
