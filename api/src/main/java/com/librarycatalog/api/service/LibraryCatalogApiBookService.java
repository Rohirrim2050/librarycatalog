package com.librarycatalog.api.service;

import java.util.List;

import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Book;

public interface LibraryCatalogApiBookService {

	List<Book> getBookList() throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	Book getBookDetails(String bookId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	String addBook(Book book) throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;
}
