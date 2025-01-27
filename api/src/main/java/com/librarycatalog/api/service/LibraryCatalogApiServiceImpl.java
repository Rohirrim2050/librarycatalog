package com.librarycatalog.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;

/**
 * This service acts as a facade for the specific services.
 */
@Component
@Qualifier("libraryCatalogApiService")
public class LibraryCatalogApiServiceImpl implements LibraryCatalogApiService {

	@Autowired
	@Qualifier("libraryCatalogApiBookService")
	private LibraryCatalogApiBookService libraryCatalogApiBookService;

	@Autowired
	@Qualifier("libraryCatalogApiBorrowerService")
	private LibraryCatalogApiBorrowerService libraryCatalogApiBorrowerService;

	@Autowired
	@Qualifier("libraryCatalogApiBorrowedBookService")
	private LibraryCatalogApiBorrowedBookService libraryCatalogApiBorrowedBookService;

	@Override
	public List<Book> getBookList()
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBookService.getBookList();
	}

	@Override
	public Book getBookDetails(String bookId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBookService.getBookDetails(bookId);
	}

	@Override
	public String addBook(Book book)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBookService.addBook(book);
	}

	@Override
	public List<Borrower> getBorrowerList()
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBorrowerService.getBorrowerList();
	}

	@Override
	public Borrower getBorrowerDetails(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBorrowerService.getBorrowerDetails(borrowerId);
	}

	@Override
	public String addBorrower(Borrower borrower)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBorrowerService.addBorrower(borrower);
	}

	@Override
	public List<Book> getBorrowedBookList(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBorrowedBookService.getBorrowedBookList(borrowerId);
	}

	@Override
	public String borrowBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBorrowedBookService.borrowBook(borrowerRecord);
	}

	@Override
	public String returnBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		return libraryCatalogApiBorrowedBookService.returnBook(borrowerRecord);
	}
}
