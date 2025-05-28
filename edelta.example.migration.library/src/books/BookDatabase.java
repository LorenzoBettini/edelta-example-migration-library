/**
 */
package books;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Book Database</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link books.BookDatabase#getBooks <em>Books</em>}</li>
 * </ul>
 *
 * @see books.BooksPackage#getBookDatabase()
 * @model
 * @generated
 */
public interface BookDatabase extends EObject {
	/**
	 * Returns the value of the '<em><b>Books</b></em>' containment reference list.
	 * The list contents are of type {@link books.Book}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Books</em>' containment reference list.
	 * @see books.BooksPackage#getBookDatabase_Books()
	 * @model containment="true"
	 * @generated
	 */
	EList<Book> getBooks();

} // BookDatabase
