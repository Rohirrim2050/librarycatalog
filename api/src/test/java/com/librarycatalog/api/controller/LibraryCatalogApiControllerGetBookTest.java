package com.librarycatalog.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.librarycatalog.api.model.Book;

/**
 * This contains integration tests for retrieving book using the provided test
 * data file.
 */
public class LibraryCatalogApiControllerGetBookTest {

	@Test
	public void testRetrieveBooksSuccess() {
		String responseString = LibraryCatalogApiControllerTestUtil.getResponseString("http://localhost:8080/books",
				HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertNotNull(response.getData());

		@SuppressWarnings("unchecked")
		List<Book> bookList = (List<Book>) response.getData();
		assertTrue(bookList.size() > 0);
	}

	@Test
	public void testRetrieveBookSuccess() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.getResponseString("http://localhost:8080/book?bookid=1", HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertNotNull(response.getData());

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) response.getData();
		assertEquals(map.get("bookId"), "1");
		assertEquals(map.get("title"), "The Fellowship of the Ring");
		assertEquals(map.get("author"), "J.R.R. Tolkien");
	}

	@Test
	public void testRetrieveBookSuccessFromCache() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.getResponseString("http://localhost:8080/book?bookid=1", HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertNotNull(response.getData());

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) response.getData();
		assertEquals(map.get("bookId"), "1");
		assertEquals(map.get("title"), "The Fellowship of the Ring");
		assertEquals(map.get("author"), "J.R.R. Tolkien");
	}

	@Test
	public void testRetrieveBookWithInvalidBookID() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.getResponseString("http://localhost:8080/book?bookid=invalidid", HttpURLConnection.HTTP_NOT_FOUND);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Cannot find book ID [invalidid]", response.getMessage());
	}

	@Test
	public void testRetrieveBookWithoutProvidedBookID() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.getResponseString("http://localhost:8080/book?bookid=", HttpURLConnection.HTTP_NOT_FOUND);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid book ID []", response.getMessage());
	}

	@Test
	public void testRetrieveBookWithInvalidCharactersInBookID() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.getResponseString("http://localhost:8080/book?bookid=book_id123", HttpURLConnection.HTTP_NOT_FOUND);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid book ID [book_id123]", response.getMessage());
	}
}
