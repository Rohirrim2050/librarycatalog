package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.librarycatalog.api.cache.LibraryCatalogApiCache;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.util.LibraryCatalogApiServiceUtil;

/**
 * This book service performs input validation before performing the actual
 * work. Any exception caught in this layer will be wrapped in another
 * exception.
 */
@Component
@Qualifier("libraryCatalogApiBookService")
public class LibraryCatalogApiBookServiceImpl implements LibraryCatalogApiBookService {

	private static final Logger logger = LogManager.getLogger(LibraryCatalogApiBookServiceImpl.class);

	@Autowired
	@Qualifier("libraryCatalogApiDataServiceFile")
	private LibraryCatalogApiDataService libraryCatalogApiDataService;

	@Autowired
	@Qualifier("libraryCatalogApiCache")
	private LibraryCatalogApiCache libraryCatalogApiCache;

	@Override
	public List<Book> getBookList()
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		List<Book> bookList = null;

		try {
			bookList = libraryCatalogApiDataService.getBookList();
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}

		if (bookList == null || bookList.isEmpty()) {
			String message = "Empty book list";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		return bookList;
	}

	@Override
	public Book getBookDetails(String bookId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		if (!LibraryCatalogApiServiceUtil.isValidId(bookId)) {
			String message = "Invalid book ID [" + bookId + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		Book book = null;

		// Read from cache.
		Map<String, Book> bookMap = libraryCatalogApiCache.getBookMap();
		if (bookMap.containsKey(bookId)) {
			logger.info("Book ID [" + bookId + "] found in cache");
			book = bookMap.get(bookId);
			return book;
		}

		// Not found in cache. Read from main source.
		try {
			book = libraryCatalogApiDataService.getBookDetails(bookId);
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}

		if (book == null) {
			String message = "Cannot find book ID [" + bookId + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		} else {
			// Add to cache.
			bookMap.put(bookId, book);
		}

		return book;
	}

	@Override
	public String addBook(Book book)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		if (StringUtils.isBlank(book.getTitle())) {
			String message = "Invalid book title [" + book.getTitle() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		if (StringUtils.isBlank(book.getAuthor())) {
			String message = "Invalid book author [" + book.getAuthor() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		// Check if book already exists from the main source.
		if (bookExists(book)) {
			String message = "Book already exists [" + book.getTitle() + "][" + book.getAuthor() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		try {
			String newBookId = libraryCatalogApiDataService.addBook(book);

			if (newBookId == null) {
				String message = "Unable to add book [" + book.getTitle() + "][" + book.getAuthor() + "]";
				logger.error(message);
				throw new LibraryCatalogApiServiceValidationException(message);
			}

			book.setBookId(newBookId);

			// Update cache.
			Map<String, Book> bookMap = libraryCatalogApiCache.getBookMap();
			bookMap.put(newBookId, book);

			return newBookId;
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}
	}

	/**
	 * This checks if the book exists by checking if it has the same title and
	 * author from one of the books from the main source.
	 * 
	 * @param book Book instance
	 * @return boolean
	 */
	private boolean bookExists(Book book)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		List<Book> bookList = getBookList();
		for (Book currentBook : bookList) {
			if (LibraryCatalogApiServiceUtil.isSame(currentBook.getTitle(), book.getTitle())
					&& LibraryCatalogApiServiceUtil.isSame(currentBook.getAuthor(), book.getAuthor()))
				return true;
		}
		return false;
	}
}
