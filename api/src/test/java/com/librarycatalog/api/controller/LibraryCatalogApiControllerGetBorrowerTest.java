package com.librarycatalog.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.librarycatalog.api.model.Borrower;

/**
 * This contains integration tests for retrieving borrower using the provided
 * test data file.
 */
public class LibraryCatalogApiControllerGetBorrowerTest {

	@Test
	public void testRetrieveBorrowersSuccess() {
		String responseString = LibraryCatalogApiControllerTestUtil.getResponseString("http://localhost:8080/borrowers",
				HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertNotNull(response.getData());

		@SuppressWarnings("unchecked")
		List<Borrower> borrowerList = (List<Borrower>) response.getData();
		assertTrue(borrowerList.size() > 0);
	}

	@Test
	public void testRetrieveBorrowerSuccess() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.getResponseString("http://localhost:8080/borrower?borrowerid=1", HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertNotNull(response.getData());

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) response.getData();
		assertEquals(map.get("borrowerId"), "1");
		assertEquals(map.get("borrowerName"), "Clark Kent");
	}

	@Test
	public void testRetrieveBorrowerWithInvalidBorrowerID() {
		String responseString = LibraryCatalogApiControllerTestUtil.getResponseString(
				"http://localhost:8080/borrower?borrowerid=invalidid", HttpURLConnection.HTTP_NOT_FOUND);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Cannot find borrower ID [invalidid]", response.getMessage());
	}

	@Test
	public void testRetrieveBorrowerWithoutProvidedBorrowerID() {
		String responseString = LibraryCatalogApiControllerTestUtil
				.getResponseString("http://localhost:8080/borrower?borrowerid=", HttpURLConnection.HTTP_NOT_FOUND);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrower ID []", response.getMessage());
	}

	@Test
	public void testRetrieveBorrowerhInvalidCharactersInBorrowerID() {
		String responseString = LibraryCatalogApiControllerTestUtil.getResponseString(
				"http://localhost:8080/borrower?borrowerid=borrower_id123", HttpURLConnection.HTTP_NOT_FOUND);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrower ID [borrower_id123]", response.getMessage());
	}
}
