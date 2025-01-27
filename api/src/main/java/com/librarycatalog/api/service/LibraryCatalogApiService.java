package com.librarycatalog.api.service;

import java.util.List;

import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;

public interface LibraryCatalogApiService {
	/**
	 * This returns the list of Book instances.
	 * 
	 * @return Book list
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	List<Book> getBookList() throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This returns the list of Borrower instances.
	 * 
	 * @return Borrower list
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	List<Borrower> getBorrowerList()
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This returns the Book instance using the provided Book ID.
	 * 
	 * @param bookId Book ID
	 * @return Book instance
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	Book getBookDetails(String bookId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This returns the Borrower instance using the provided Borrower ID.
	 * 
	 * @param borrowerId Borrower ID
	 * @return Borrower instance
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	Borrower getBorrowerDetails(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This saves a Book instance.
	 * 
	 * @param book Book instance
	 * @return transaction ID
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	String addBook(Book book) throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This saves a Borrower instance.
	 * 
	 * @param borrower Borrower instance
	 * @return transaction ID
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	String addBorrower(Borrower borrower)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This returns a list of borrowed books by the provided borrower ID.
	 * 
	 * @param borrowerId borrower ID
	 * @return List<Book> instance
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	List<Book> getBorrowedBookList(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This saves the BorrowerRecord instance when borrowing a book.
	 * 
	 * @param borrowerRecord BorrowerRecord instance
	 * @return transaction ID
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	String borrowBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	/**
	 * This updates the BorrowerRecord instance when returning a book.
	 * 
	 * @param borrowerRecord BorrowerRecord instance
	 * @return transaction ID
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	String returnBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;
}
