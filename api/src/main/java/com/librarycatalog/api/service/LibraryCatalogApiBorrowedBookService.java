package com.librarycatalog.api.service;

import java.util.List;

import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.BorrowerRecord;

public interface LibraryCatalogApiBorrowedBookService {
	String DATE_FORMAT = "MMddyyyy";

	List<Book> getBorrowedBookList(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	String borrowBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	String returnBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

}
