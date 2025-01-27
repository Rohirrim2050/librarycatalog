package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.librarycatalog.api.cache.LibraryCatalogApiCache;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;
import com.librarycatalog.api.util.LibraryCatalogApiServiceUtil;

/**
 * This borrowed-book service performs input validation before performing the
 * actual work. Any exception caught in this layer will be wrapped in another
 * exception.
 */
@Component
@Qualifier("libraryCatalogApiBorrowedBookService")
public class LibraryCatalogApiBorrowedBookServiceImpl implements LibraryCatalogApiBorrowedBookService {

	private static final Logger logger = LogManager.getLogger(LibraryCatalogApiBorrowedBookServiceImpl.class);

	@Autowired
	@Qualifier("libraryCatalogApiDataServiceFile")
	private LibraryCatalogApiDataService libraryCatalogApiDataService;

	@Autowired
	@Qualifier("libraryCatalogApiCache")
	private LibraryCatalogApiCache libraryCatalogApiCache;

	@Autowired
	@Qualifier("libraryCatalogApiBookService")
	private LibraryCatalogApiBookService libraryCatalogApiBookService;

	@Autowired
	@Qualifier("libraryCatalogApiBorrowerService")
	private LibraryCatalogApiBorrowerService libraryCatalogApiBorrowerService;

	@Override
	public List<Book> getBorrowedBookList(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		if (!LibraryCatalogApiServiceUtil.isValidId(borrowerId)) {
			String message = "Invalid borrower ID [" + borrowerId + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		Borrower borrower = libraryCatalogApiBorrowerService.getBorrowerDetails(borrowerId);
		if (borrower == null) {
			String message = "Cannot find borrower ID [" + borrowerId + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		List<Book> bookList = null;

		// Check the cache.
		Map<String, List<BorrowerRecord>> borrowerRecordMap = libraryCatalogApiCache.getBorrowerRecordMap();
		if (borrowerRecordMap.containsKey(borrowerId)) {
			bookList = new ArrayList<Book>();

			List<BorrowerRecord> borrowerRecordList = borrowerRecordMap.get(borrowerId);

			for (BorrowerRecord borrowerRecord : borrowerRecordList) {
				bookList.add(libraryCatalogApiBookService.getBookDetails(borrowerRecord.getBookId()));
			}

			return bookList;
		}

		// Not found in cache. Read from main source.
		bookList = new ArrayList<Book>();

		List<BorrowerRecord> borrowerRecordList = null;

		try {
			borrowerRecordList = libraryCatalogApiDataService.getBorrowerRecordUnreturnedList(borrowerId);
			if (borrowerRecordList == null) {
				String message = "Cannot find any borrowed books for borrower ID [" + borrowerId + "]";
				logger.error(message);
				throw new LibraryCatalogApiServiceValidationException(message);
			}

			for (BorrowerRecord borrowerRecord : borrowerRecordList) {
				bookList.add(libraryCatalogApiBookService.getBookDetails(borrowerRecord.getBookId()));
			}

			// Add to cache.
			borrowerRecordMap.put(borrowerId, borrowerRecordList);
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}

		return bookList;
	}

	@Override
	public String borrowBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		if (!LibraryCatalogApiServiceUtil.isValidId(borrowerRecord.getBorrowerId())) {
			String message = "Invalid borrower ID [" + borrowerRecord.getBorrowerId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		if (!LibraryCatalogApiServiceUtil.isValidId(borrowerRecord.getBookId())) {
			String message = "Invalid book ID [" + borrowerRecord.getBookId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		if (StringUtils.isBlank(borrowerRecord.getBorrowDate())
				|| !GenericValidator.isDate(borrowerRecord.getBorrowDate(), DATE_FORMAT, true)) {
			String message = "Invalid borrow date [" + borrowerRecord.getBorrowDate() + "]. Valid format ["
					+ DATE_FORMAT + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		Borrower borrower = libraryCatalogApiBorrowerService.getBorrowerDetails(borrowerRecord.getBorrowerId());
		if (borrower == null) {
			String message = "Cannot find borrower ID [" + borrowerRecord.getBorrowerId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		Book book = libraryCatalogApiBookService.getBookDetails(borrowerRecord.getBookId());
		if (book == null) {
			String message = "Cannot find book ID [" + borrowerRecord.getBookId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		BorrowerRecord borrowerRecordCache = getBorrowerRecordInCache(borrowerRecord.getBookId(), null, false);
		if (borrowerRecordCache != null) {
			String message = "Book [" + borrowerRecord.getBookId() + "] is already borrowed";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		BorrowerRecord borrowerRecordMainSource = getBookBorrowed(borrowerRecord.getBookId(), null, false);
		if (borrowerRecordMainSource != null) {
			String message = "Book [" + borrowerRecord.getBookId() + "] is already borrowed";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		try {
			String newTransactionId = libraryCatalogApiDataService.addBorrowerRecord(borrowerRecord);

			if (newTransactionId == null) {
				String message = "Unable to add new borrower record for borrower ID [" + borrowerRecord.getBorrowerId()
						+ "] and book ID [" + borrowerRecord.getBookId() + "]";
				logger.error(message);
				throw new LibraryCatalogApiServiceValidationException(message);
			}

			borrowerRecord.setTransactionId(newTransactionId);

			// Update cache
			Map<String, List<BorrowerRecord>> borrowerRecordMap = libraryCatalogApiCache.getBorrowerRecordMap();
			List<BorrowerRecord> borrowerRecordList = borrowerRecordMap.get(borrowerRecord.getBorrowerId());
			if (borrowerRecordList == null) {
				borrowerRecordList = new ArrayList<BorrowerRecord>();
			}
			borrowerRecordList.add(borrowerRecord);

			return newTransactionId;
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}
	}

	/**
	 * This searches for the BorrowerRecord instance from cache that matches the
	 * book ID. If search includes the borrower ID, then the isBorrowedByBorrower
	 * flag must be set to true.
	 * 
	 * @param bookId               Book ID to search
	 * @param borrowerId           Borrower ID to search
	 * @param isBorrowedByBorrower boolean flag if borrower ID is included in search
	 * @return BorrowerRecord
	 */
	private BorrowerRecord getBorrowerRecordInCache(String bookId, String borrowerId, boolean isBorrowedByBorrower) {

		Map<String, List<BorrowerRecord>> borrowerRecordMap = libraryCatalogApiCache.getBorrowerRecordMap();

		if (!borrowerRecordMap.isEmpty()) {
			List<BorrowerRecord> borrowerRecordList = null;
			Iterator<Map.Entry<String, List<BorrowerRecord>>> iterator = borrowerRecordMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, List<BorrowerRecord>> entry = iterator.next();

				borrowerRecordList = entry.getValue();

				for (BorrowerRecord borrowerRecord : borrowerRecordList) {
					if (borrowerRecord.getBookId().equals(bookId) && (!isBorrowedByBorrower
							|| (isBorrowedByBorrower && borrowerRecord.getBorrowerId().equals(borrowerId)))) {
						return borrowerRecord;
					}
				}
			}
		}

		return null;
	}

	/**
	 * This searches for the BorrowerRecord instance from the main source that
	 * matches the book ID. If search includes the borrower ID, then the
	 * isBorrowedByBorrower flag must be set to true.
	 * 
	 * @param bookId               Book ID to search
	 * @param borrowerId           Borrower ID to search
	 * @param isBorrowedByBorrower boolean flag if borrower ID is included in search
	 * @return BorrowerRecord
	 * @return
	 * @throws LibraryCatalogApiServiceException
	 * @throws LibraryCatalogApiServiceValidationException
	 */
	private BorrowerRecord getBookBorrowed(String bookId, String borrowerId, boolean isBorrowedByBorrower)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		try {
			List<BorrowerRecord> borrowerRecordList = libraryCatalogApiDataService.getBorrowerRecordUnreturnedList();
			if (borrowerRecordList == null) {
				return null;
			}

			for (BorrowerRecord borrowerRecord : borrowerRecordList) {
				if (borrowerRecord.getBookId().equals(bookId) && (!isBorrowedByBorrower
						|| (isBorrowedByBorrower && borrowerRecord.getBorrowerId().equals(borrowerId)))) {
					return borrowerRecord;
				}
			}
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}

		return null;
	}

	@Override
	public String returnBook(BorrowerRecord borrowerRecord)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		if (!LibraryCatalogApiServiceUtil.isValidId(borrowerRecord.getBorrowerId())) {
			String message = "Invalid borrower ID [" + borrowerRecord.getBorrowerId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		if (!LibraryCatalogApiServiceUtil.isValidId(borrowerRecord.getBookId())) {
			String message = "Invalid book ID [" + borrowerRecord.getBookId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		if (StringUtils.isBlank(borrowerRecord.getReturnDate())
				|| !GenericValidator.isDate(borrowerRecord.getReturnDate(), DATE_FORMAT, true)) {
			String message = "Invalid return date [" + borrowerRecord.getReturnDate() + "]. Valid format ["
					+ DATE_FORMAT + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		Borrower borrower = libraryCatalogApiBorrowerService.getBorrowerDetails(borrowerRecord.getBorrowerId());
		if (borrower == null) {
			String message = "Cannot find borrower ID [" + borrowerRecord.getBorrowerId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		Book book = libraryCatalogApiBookService.getBookDetails(borrowerRecord.getBookId());
		if (book == null) {
			String message = "Cannot find book ID [" + borrowerRecord.getBookId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		// Check borrower record from cache, then from main source.
		BorrowerRecord retrievedBorrowerRecord = getBorrowerRecordInCache(borrowerRecord.getBookId(),
				borrowerRecord.getBorrowerId(), true);
		if (retrievedBorrowerRecord == null) {
			retrievedBorrowerRecord = getBookBorrowed(borrowerRecord.getBookId(), borrowerRecord.getBorrowerId(), true);
		}

		if (retrievedBorrowerRecord == null) {
			String message = "No borrowed record for borrower ID [" + borrowerRecord.getBorrowerId() + "], book ID ["
					+ borrowerRecord.getBookId() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		if (!LibraryCatalogApiServiceUtil.isDate2AfterDate1(retrievedBorrowerRecord.getBorrowDate(),
				borrowerRecord.getReturnDate())) {
			String message = "Return date [" + borrowerRecord.getReturnDate()
					+ "] is before or same as the borrow date";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		try {
			// Set the missing details from the input parameter.
			borrowerRecord.setTransactionId(retrievedBorrowerRecord.getTransactionId());
			borrowerRecord.setBorrowDate(retrievedBorrowerRecord.getBorrowDate());

			String transactionId = libraryCatalogApiDataService.updateBorrowerRecord(borrowerRecord);

			if (transactionId == null) {
				String message = "Unable to update (return) borrower record for borrower ID ["
						+ borrowerRecord.getBorrowerId() + "] and book ID [" + borrowerRecord.getBookId() + "]";
				logger.error(message);
				throw new LibraryCatalogApiServiceValidationException(message);
			}

			removeBorrowerRecordFromCache(borrowerRecord);

			return transactionId;
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}
	}

	/**
	 * This removes the borrower record from cache, if found, by using iterator to
	 * safely delete it.
	 * 
	 * @param borrowerRecord borrower record to delete
	 */
	private void removeBorrowerRecordFromCache(BorrowerRecord borrowerRecord) {
		Map<String, List<BorrowerRecord>> borrowerRecordMap = libraryCatalogApiCache.getBorrowerRecordMap();
		List<BorrowerRecord> borrowerRecordList = borrowerRecordMap.get(borrowerRecord.getBorrowerId());
		if (borrowerRecordList != null) {
			Iterator<BorrowerRecord> iterator = borrowerRecordList.iterator();
			while (iterator.hasNext()) {
				BorrowerRecord element = iterator.next();
				if (borrowerRecord.getBookId().equals(element.getBookId())
						&& borrowerRecord.getBorrowerId().equals(element.getBorrowerId())) {
					iterator.remove();
					break;
				}
			}
		}
	}
}
