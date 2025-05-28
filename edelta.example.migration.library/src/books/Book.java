/**
 */
package books;

import library.Library;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Book</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link books.Book#getTitle <em>Title</em>}</li>
 *   <li>{@link books.Book#getDatabase <em>Database</em>}</li>
 *   <li>{@link books.Book#getLibraries <em>Libraries</em>}</li>
 * </ul>
 *
 * @see books.BooksPackage#getBook()
 * @model
 * @generated
 */
public interface Book extends EObject {
	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see books.BooksPackage#getBook_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link books.Book#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Database</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link books.BookDatabase#getBooks <em>Books</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Database</em>' container reference.
	 * @see #setDatabase(BookDatabase)
	 * @see books.BooksPackage#getBook_Database()
	 * @see books.BookDatabase#getBooks
	 * @model opposite="books" transient="false"
	 * @generated
	 */
	BookDatabase getDatabase();

	/**
	 * Sets the value of the '{@link books.Book#getDatabase <em>Database</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database</em>' container reference.
	 * @see #getDatabase()
	 * @generated
	 */
	void setDatabase(BookDatabase value);

	/**
	 * Returns the value of the '<em><b>Libraries</b></em>' reference list.
	 * The list contents are of type {@link library.Library}.
	 * It is bidirectional and its opposite is '{@link library.Library#getBooks <em>Books</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Libraries</em>' reference list.
	 * @see books.BooksPackage#getBook_Libraries()
	 * @see library.Library#getBooks
	 * @model opposite="books"
	 * @generated
	 */
	EList<Library> getLibraries();

} // Book
