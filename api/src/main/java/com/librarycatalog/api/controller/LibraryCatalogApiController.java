package com.librarycatalog.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;
import com.librarycatalog.api.service.LibraryCatalogApiService;

@RestController
public class LibraryCatalogApiController {

	@Autowired
	@Qualifier("libraryCatalogApiService")
	private LibraryCatalogApiService libraryCatalogApiService;

	@GetMapping("/")
	public String index() {
		return "Greetings from Library Catalog Api Controller!";
	}

	@GetMapping("/books")
	public ResponseEntity<Response> getBookList() {
		try {
			List<Book> bookList = libraryCatalogApiService.getBookList();
			return ResponseEntity.ok().body(new ResponseSuccess(bookList));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/book")
	public ResponseEntity<Response> getBook(@RequestParam String bookid) {
		try {
			Book book = libraryCatalogApiService.getBookDetails(bookid);
			return ResponseEntity.ok().body(new ResponseSuccess(book));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/books")
	public ResponseEntity<Response> addBook(@RequestBody Book book) {
		try {
			String newBookId = libraryCatalogApiService.addBook(book);
			return ResponseEntity.ok().body(new ResponseSuccess("Created new book ID [" + newBookId + "]"));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/borrowers")
	public ResponseEntity<Response> getBorrowerList() {
		try {
			List<Borrower> borrowerList = libraryCatalogApiService.getBorrowerList();
			return ResponseEntity.ok().body(new ResponseSuccess(borrowerList));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/borrower")
	public ResponseEntity<Response> getBorrower(@RequestParam String borrowerid) {
		try {
			Borrower borrower = libraryCatalogApiService.getBorrowerDetails(borrowerid);
			return ResponseEntity.ok().body(new ResponseSuccess(borrower));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/borrowers")
	public ResponseEntity<Response> addBorrower(@RequestBody Borrower borrower) {
		try {
			String newBorrowerId = libraryCatalogApiService.addBorrower(borrower);
			return ResponseEntity.ok().body(new ResponseSuccess("Created new borrower ID [" + newBorrowerId + "]"));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/books/borrowing")
	public ResponseEntity<Response> getBorrowedBookList(@RequestParam String borrowerid) {
		try {
			List<Book> borrowedBookList = libraryCatalogApiService.getBorrowedBookList(borrowerid);
			return ResponseEntity.ok().body(new ResponseSuccess(borrowedBookList, "borrower ID [" + borrowerid + "]"));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/books/borrowing")
	public ResponseEntity<Response> borrowBook(@RequestBody BorrowerRecord borrowerRecord) {
		try {
			String transactionId = libraryCatalogApiService.borrowBook(borrowerRecord);
			return ResponseEntity.ok()
					.body(new ResponseSuccess("Borrowed book transaction ID [" + transactionId + "]"));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/books/returning")
	public ResponseEntity<Response> returnBook(@RequestBody BorrowerRecord borrowerRecord) {
		try {
			String transactionId = libraryCatalogApiService.returnBook(borrowerRecord);
			return ResponseEntity.ok()
					.body(new ResponseSuccess("Returned book transaction ID [" + transactionId + "]"));
		} catch (LibraryCatalogApiServiceException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (LibraryCatalogApiServiceValidationException ex) {
			return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}