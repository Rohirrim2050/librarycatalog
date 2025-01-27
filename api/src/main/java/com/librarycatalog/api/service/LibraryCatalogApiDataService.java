package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.List;

import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;

/**
 * 
 */
public interface LibraryCatalogApiDataService {
	List<Book> getBookList() throws IOException;

	String addBook(Book book) throws IOException;

	Book getBookDetails(String bookId) throws IOException;

	List<Borrower> getBorrowerList() throws IOException;

	String addBorrower(Borrower borrower) throws IOException;

	Borrower getBorrowerDetails(String borrowerId) throws IOException;

	List<BorrowerRecord> getBorrowerRecordUnreturnedList(String borrowerId) throws IOException;

	List<BorrowerRecord> getBorrowerRecordUnreturnedList() throws IOException;

	String addBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException;

	String updateBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException;
}
