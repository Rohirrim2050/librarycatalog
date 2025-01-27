package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;

/**
 * This data service acts as a facade to the specific data services.
 */
@Component
@Qualifier("libraryCatalogApiDataServiceFile")
public class LibraryCatalogApiDataServiceFileImpl implements LibraryCatalogApiDataService {

	@Autowired
	@Qualifier("libraryCatalogApiDataServiceFileBook")
	private LibraryCatalogApiDataServiceFileBook libraryCatalogApiDataServiceFileBook;

	@Autowired
	@Qualifier("libraryCatalogApiDataServiceFileBorrower")
	private LibraryCatalogApiDataServiceFileBorrower libraryCatalogApiDataServiceFileBorrower;

	@Autowired
	@Qualifier("libraryCatalogApiDataServiceFileBorrowerRecord")
	private LibraryCatalogApiDataServiceFileBorrowerRecord libraryCatalogApiDataServiceFileBorrowerRecord;

	@Override
	public List<Book> getBookList() throws IOException {
		return libraryCatalogApiDataServiceFileBook.getBookList();
	}

	@Override
	public Book getBookDetails(String bookId) throws IOException {
		return libraryCatalogApiDataServiceFileBook.getBookDetails(bookId);
	}

	@Override
	public String addBook(Book book) throws IOException {
		return libraryCatalogApiDataServiceFileBook.addBook(book);
	}

	/**
	 * This retrieves the data string list and converts into Borrower instances.
	 */
	@Override
	public List<Borrower> getBorrowerList() throws IOException {
		return libraryCatalogApiDataServiceFileBorrower.getBorrowerList();
	}

	@Override
	public Borrower getBorrowerDetails(String borrowerId) throws IOException {
		return libraryCatalogApiDataServiceFileBorrower.getBorrowerDetails(borrowerId);
	}

	@Override
	public String addBorrower(Borrower borrower) throws IOException {
		return libraryCatalogApiDataServiceFileBorrower.addBorrower(borrower);
	}

	@Override
	public List<BorrowerRecord> getBorrowerRecordUnreturnedList(String borrowerId) throws IOException {
		return libraryCatalogApiDataServiceFileBorrowerRecord.getBorrowerRecordUnreturnedList(borrowerId);
	}

	@Override
	public List<BorrowerRecord> getBorrowerRecordUnreturnedList() throws IOException {
		return libraryCatalogApiDataServiceFileBorrowerRecord.getBorrowerRecordUnreturnedList();
	}

	@Override
	public String addBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException {
		return libraryCatalogApiDataServiceFileBorrowerRecord.addBorrowerRecord(borrowerRecord);
	}

	@Override
	public String updateBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException {
		return libraryCatalogApiDataServiceFileBorrowerRecord.updateBorrowerRecord(borrowerRecord);
	}
}
