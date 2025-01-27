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
 * This contains integration tests for creating borrower, run in specific
 * sequence, using the provided test data file. Because this uses actual file
 * that is updated by test cases, there will be a need to update the file to
 * prevent them from growing.
 * 
 * Steps before running this test: <br/>
 * 1) If the application is running, stop it. <br/>
 * 2) Delete any data that were added during the previous run. From the local
 * run, the test file will be located here: <br/>
 * ../api/target/classes/data/borrowers.txt <br/>
 * 2) Restart the application. <br/>
 * 3) Run the entire test.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryCatalogApiControllerPostBorrowerTest {

	private static String borrowerName;

	@BeforeAll
	static void setUp() {
		borrowerName = "Borrower " + generateText();
	}

	@Test
	@Order(1)
	public void testCreateBorrowerWithoutRequest() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/borrowers", "", HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNull(response);
	}

	@Test
	@Order(2)
	public void testCreateBorrowerWithMissingData() {
		String request = "{\"borrowerName\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/borrowers", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrower name []", response.getMessage());
	}

	@Test
	@Order(3)
	public void testCreateBorrowerSuccess() {
		String request = "{\"borrowerName\": \"" + borrowerName + "\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/borrowers", request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Created new borrower ID [BR"));
	}

	@Test
	@Order(4)
	public void testCreateBorrowerThatExists() {
		String request = "{\"borrowerName\": \"" + borrowerName + "\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/borrowers", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getMessage()).startsWith("Borrower already exists [Borrower "));
	}

	private static String generateText() {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		return formatter.format(currentDate);
	}
}
