package com.librarycatalog.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * This contains integration tests for creating book, run in specific sequence,
 * using the provided test data file. Because this uses actual file that is
 * updated by test cases, there will be a need to update the file to prevent
 * them from growing.
 * 
 * Steps before running this test: <br/>
 * 1) If the application is running, stop it. <br/>
 * 2) Delete any data that were added during the previous run. From the local
 * run, the test file will be located here: <br/>
 * ../api/target/classes/data/books.txt <br/>
 * 2) Restart the application. <br/>
 * 3) Run the entire test.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryCatalogApiControllerPostBookTest {

	private static String bookTitle;
	private static String bookAuthor;

	@BeforeAll
	static void setUp() {
		bookTitle = "Book " + generateText();
		bookAuthor = "Author " + generateText();
	}

	@Test
	@Order(1)
	public void testCreateBookWithoutRequest() {
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString("http://localhost:8080/books",
				"", HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNull(response);
	}

	@Test
	@Order(2)
	public void testCreateBookWithMissingTitle() {
		String request = "{\"title\": \"\", \"author\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString("http://localhost:8080/books",
				request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid book title []", response.getMessage());
	}

	@Test
	@Order(3)
	public void testCreateBookWithMissingAuthor() {
		String request = "{\"title\": \"\", \"author\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString("http://localhost:8080/books",
				request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid book title []", response.getMessage());
	}

	@Test
	@Order(4)
	public void testCreateBookSuccess() {
		String request = "{\"title\": \"" + bookTitle + "\", \"author\": \"" + bookAuthor + "\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString("http://localhost:8080/books",
				request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Created new book ID [BK"));
	}

	@Test
	@Order(5)
	public void testCreateBookThatAlreadyExists() {
		String request = "{\"title\": \"" + bookTitle + "\", \"author\": \"" + bookAuthor + "\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString("http://localhost:8080/books",
				request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getMessage()).startsWith("Book already exists [Book"));
	}

	private static String generateText() {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		return formatter.format(currentDate);
	}
}
