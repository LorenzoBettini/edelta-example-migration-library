/**
 */
package library;

import books.Book;

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
 *   <li>{@link library.Library#getBooks <em>Books</em>}</li>
 * </ul>
 *
 * @see library.LibraryPackage#getLibrary()
 * @model
 * @generated
 */
public interface Library extends EObject {
	/**
	 * Returns the value of the '<em><b>Books</b></em>' reference list.
	 * The list contents are of type {@link books.Book}.
	 * It is bidirectional and its opposite is '{@link books.Book#getLibraries <em>Libraries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Books</em>' reference list.
	 * @see library.LibraryPackage#getLibrary_Books()
	 * @see books.Book#getLibraries
	 * @model opposite="libraries"
	 * @generated
	 */
	EList<Book> getBooks();

} // Library
