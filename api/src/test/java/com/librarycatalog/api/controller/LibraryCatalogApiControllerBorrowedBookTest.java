package com.librarycatalog.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.HttpURLConnection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * This contains integration tests for borrowing and returning book, run in
 * specific sequence, using the provided test data files. Because this uses
 * actual file that is updated by test cases, there will be a need to update the
 * file to prevent them from growing. This also uses books.txt and borrowers.txt
 * as input files.
 * 
 * Steps before running this test: <br/>
 * 1) If the application is running, stop it. <br/>
 * 2) Delete any data that were added during the previous run. From the local
 * run, the test file will be located here: <br/>
 * ../api/target/classes/data/borrowers_records.txt <br/>
 * 2) Restart the application. <br/>
 * 3) Run the entire test.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryCatalogApiControllerBorrowedBookTest {

	@BeforeAll
	static void setUp() {
	}

	@Test
	@Order(1)
	public void testGetBorrowedBookListWithMissingBorrowerID() {
		String responseString = LibraryCatalogApiControllerTestUtil.getResponseString(
				"http://localhost:8080/books/borrowing?borrowerid=", HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrower ID []", response.getMessage());
	}

	@Test
	@Order(2)
	public void testGetBorrowedBookListWithInvalidBorrowerID() {
		String responseString = LibraryCatalogApiControllerTestUtil.getResponseString(
				"http://localhost:8080/books/borrowing?borrowerid=invalidid", HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Cannot find borrower ID [invalidid]", response.getMessage());
	}

	@Test
	@Order(3)
	public void testGetBorrowedBookListWithValidBorrowerButNoBorrowedBooks() {
		String responseString = LibraryCatalogApiControllerTestUtil.getResponseString(
				"http://localhost:8080/books/borrowing?borrowerid=1", HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Cannot find any borrowed books for borrower ID [1]", response.getMessage());
	}

	@Test
	@Order(4)
	public void testBorrowBookWithMissingBorrowerId() {
		String request = "{\"borrowerId\": \"\", \"bookId\": \"\", \"borrowDate\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrower ID []", response.getMessage());
	}

	@Test
	@Order(5)
	public void testBorrowBookWithInvalidBorrowerId() {
		String request = "{\"borrowerId\": \"%$^@#$@\", \"bookId\": \"\", \"borrowDate\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrower ID [%$^@#$@]", response.getMessage());
	}

	@Test
	@Order(6)
	public void testBorrowBookWithMissingBookId() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"\", \"borrowDate\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid book ID []", response.getMessage());
	}

	@Test
	@Order(7)
	public void testBorrowBookWithInvalidBookId() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"%$^@#$@\", \"borrowDate\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid book ID [%$^@#$@]", response.getMessage());
	}

	@Test
	@Order(8)
	public void testBorrowBookWithMissingBorrowDate() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"borrowDate\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrow date []. Valid format [MMddyyyy]", response.getMessage());
	}

	@Test
	@Order(9)
	public void testBorrowBookWithInvalidBorrowDate() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"borrowDate\": \"invalidDate\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrow date [invalidDate]. Valid format [MMddyyyy]", response.getMessage());
	}

	@Test
	@Order(10)
	public void testBorrowBookWithInvalidBorrowDateFormat() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"borrowDate\": \"30122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrow date [30122025]. Valid format [MMddyyyy]", response.getMessage());
	}

	@Test
	@Order(11)
	public void testBorrower1BorrowBook1ThatIsNotYetBorrowed() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"borrowDate\": \"02122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Borrowed book transaction ID [TR"));
	}

	@Test
	@Order(12)
	public void testBorrower1BorrowBook2ThatIsNotYetBorrowed() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"2\", \"borrowDate\": \"02122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Borrowed book transaction ID [TR"));
	}

	@Test
	@Order(13)
	public void testBorrower2BorrowBook3ThatIsNotYetBorrowed() {
		String request = "{\"borrowerId\": \"2\", \"bookId\": \"3\", \"borrowDate\": \"02122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Borrowed book transaction ID [TR"));
	}

	@Test
	@Order(14)
	public void testBorrower1BorrowBook1ThatIsAlreadyBorrowed() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"borrowDate\": \"03122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Book [1] is already borrowed", response.getMessage());
	}

	@Test
	@Order(15)
	public void testtBorrower2BorrowBook1ThatIsAlreadyBorrowed() {
		String request = "{\"borrowerId\": \"2\", \"bookId\": \"1\", \"borrowDate\": \"04122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/borrowing", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Book [1] is already borrowed", response.getMessage());
	}

	@Test
	@Order(16)
	public void testBorrower1ReturnBookWithEmptyData() {
		String request = "{\"borrowerId\": \"\", \"bookId\": \"\", \"returnDate\": \"\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid borrower ID []", response.getMessage());
	}

	@Test
	@Order(17)
	public void testBorrower1ReturnBook3WithInvalidReturnDate() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"3\", \"returnDate\": \"31202025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Invalid return date [31202025]. Valid format [MMddyyyy]", response.getMessage());
	}

	@Test
	@Order(18)
	public void testBorrower1ReturnBook3ThatIsNotBorrowed() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"3\", \"returnDate\": \"03032025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("No borrowed record for borrower ID [1], book ID [3]", response.getMessage());
	}

	@Test
	@Order(19)
	public void testBorrower1ReturnBook1WithReturnDateSameAsBorrowDate() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"returnDate\": \"02122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Return date [02122025] is before or same as the borrow date", response.getMessage());
	}

	@Test
	@Order(20)
	public void testBorrower1ReturnBook1WithReturnDateEarlierThanBorrowDate() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"returnDate\": \"01122025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil.postResponseString(
				"http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_BAD_REQUEST);
		ResponseError response = LibraryCatalogApiControllerTestUtil.createResponseError(responseString);

		assertNotNull(response);
		assertEquals("Return date [01122025] is before or same as the borrow date", response.getMessage());
	}

	@Test
	@Order(21)
	public void testBorrower1ReturnBook1Success() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"1\", \"returnDate\": \"02152025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Returned book transaction ID [TR"));
	}

	@Test
	@Order(22)
	public void testBorrower1ReturnBook2Success() {
		String request = "{\"borrowerId\": \"1\", \"bookId\": \"2\", \"returnDate\": \"02152025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Returned book transaction ID [TR"));
	}

	@Test
	@Order(23)
	public void testBorrower2ReturnBook3Success() {
		String request = "{\"borrowerId\": \"2\", \"bookId\": \"3\", \"returnDate\": \"02152025\"}";
		String responseString = LibraryCatalogApiControllerTestUtil
				.postResponseString("http://localhost:8080/books/returning", request, HttpURLConnection.HTTP_OK);
		ResponseSuccess response = LibraryCatalogApiControllerTestUtil.createResponseSuccess(responseString);

		assertNotNull(response);
		assertTrue(((String) response.getData()).startsWith("Returned book transaction ID [TR"));
	}
}
