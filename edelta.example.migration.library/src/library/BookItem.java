/**
 */
package library;

import books.Book;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Book Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link library.BookItem#getBook <em>Book</em>}</li>
 *   <li>{@link library.BookItem#getLibrary <em>Library</em>}</li>
 *   <li>{@link library.BookItem#getNumOfCopies <em>Num Of Copies</em>}</li>
 * </ul>
 *
 * @see library.LibraryPackage#getBookItem()
 * @model
 * @generated
 */
public interface BookItem extends EObject {
	/**
	 * Returns the value of the '<em><b>Book</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link books.Book#getLibraryBookItems <em>Library Book Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Book</em>' reference.
	 * @see #setBook(Book)
	 * @see library.LibraryPackage#getBookItem_Book()
	 * @see books.Book#getLibraryBookItems
	 * @model opposite="libraryBookItems" required="true"
	 * @generated
	 */
	Book getBook();

	/**
	 * Sets the value of the '{@link library.BookItem#getBook <em>Book</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Book</em>' reference.
	 * @see #getBook()
	 * @generated
	 */
	void setBook(Book value);

	/**
	 * Returns the value of the '<em><b>Library</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link library.Library#getBookItems <em>Book Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Library</em>' container reference.
	 * @see #setLibrary(Library)
	 * @see library.LibraryPackage#getBookItem_Library()
	 * @see library.Library#getBookItems
	 * @model opposite="bookItems" required="true" transient="false"
	 * @generated
	 */
	Library getLibrary();

	/**
	 * Sets the value of the '{@link library.BookItem#getLibrary <em>Library</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Library</em>' container reference.
	 * @see #getLibrary()
	 * @generated
	 */
	void setLibrary(Library value);

	/**
	 * Returns the value of the '<em><b>Num Of Copies</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Num Of Copies</em>' attribute.
	 * @see #setNumOfCopies(int)
	 * @see library.LibraryPackage#getBookItem_NumOfCopies()
	 * @model default="1"
	 * @generated
	 */
	int getNumOfCopies();

	/**
	 * Sets the value of the '{@link library.BookItem#getNumOfCopies <em>Num Of Copies</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Num Of Copies</em>' attribute.
	 * @see #getNumOfCopies()
	 * @generated
	 */
	void setNumOfCopies(int value);

} // BookItem
