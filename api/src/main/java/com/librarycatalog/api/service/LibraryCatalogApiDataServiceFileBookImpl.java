package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.librarycatalog.api.model.Book;

/**
 * This data service converts file data into business objects.
 */
@Component
@Qualifier("libraryCatalogApiDataServiceFileBook")
public class LibraryCatalogApiDataServiceFileBookImpl implements LibraryCatalogApiDataServiceFileBook {

	private static final Logger logger = LogManager.getLogger(LibraryCatalogApiDataServiceFileBookImpl.class);

	private final String FILE_BOOKS = "data/books.txt";

	@Autowired
	@Qualifier("libraryCatalogApiFileDataHandler")
	private LibraryCatalogApiFileDataHandler libraryCatalogApiFileDataHandler;

	@Autowired
	@Qualifier("libraryCatalogApiIDGeneratorService")
	private LibraryCatalogApiIDGeneratorService libraryCatalogApiIDGeneratorService;

	@Override
	public List<Book> getBookList() throws IOException {
		List<String> dataList = libraryCatalogApiFileDataHandler.getDataList(FILE_BOOKS);
		if (dataList.isEmpty()) {
			logger.error("Encountered empty data from file");
			return null;
		}

		List<Book> bookList = new ArrayList<Book>();
		String[] dataArray = null;

		for (String data : dataList) {
			if (StringUtils.isBlank(data)) {
				logger.error("Encountered empty book detail");
				continue;
			}
			dataArray = data.split(",");
			if (dataArray.length == 0) {
				logger.error("Encountered empty book detail");
			} else if (dataArray.length == 3) {
				bookList.add(new Book(dataArray[0], dataArray[1], dataArray[2]));
			} else {
				logger.error("Issue with book ID [" + dataArray[0] + "]");
			}
		}
		return bookList;
	}

	@Override
	public Book getBookDetails(String bookId) throws IOException {
		List<Book> bookList = getBookList();
		if (bookList == null || bookList.isEmpty()) {
			logger.error("Encountered book list");
			return null;
		}

		for (Book book : bookList) {
			if (book.getBookId().equals(bookId))
				return book;
		}

		return null;
	}

	@Override
	public String addBook(Book book) throws IOException {
		try {
			String newBookId = libraryCatalogApiIDGeneratorService.generateBookId();
			String data = newBookId + "," + book.getTitle() + "," + book.getAuthor();
			libraryCatalogApiFileDataHandler.addNewEntry(data, FILE_BOOKS);
			logger.info("Added new book [" + newBookId + "]");
			return newBookId;
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw ioEx;
		}
	}
}
