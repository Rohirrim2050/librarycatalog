package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.List;

import com.librarycatalog.api.model.Book;

public interface LibraryCatalogApiDataServiceFileBook {

	List<Book> getBookList() throws IOException;

	Book getBookDetails(String bookId) throws IOException;

	String addBook(Book book) throws IOException;
}
