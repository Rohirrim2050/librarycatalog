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
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.util.LibraryCatalogApiServiceUtil;

/**
 * This borrower service performs input validation before performing the actual
 * work. Any exception caught in this layer will be wrapped in another
 * exception.
 */
@Component
@Qualifier("libraryCatalogApiBorrowerService")
public class LibraryCatalogApiBorrowerServiceImpl implements LibraryCatalogApiBorrowerService {

	private static final Logger logger = LogManager.getLogger(LibraryCatalogApiBorrowerServiceImpl.class);

	@Autowired
	@Qualifier("libraryCatalogApiDataServiceFile")
	private LibraryCatalogApiDataService libraryCatalogApiDataService;

	@Autowired
	@Qualifier("libraryCatalogApiCache")
	private LibraryCatalogApiCache libraryCatalogApiCache;

	@Override
	public List<Borrower> getBorrowerList()
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		List<Borrower> borrowerList = null;

		try {
			borrowerList = libraryCatalogApiDataService.getBorrowerList();
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}

		if (borrowerList == null || borrowerList.isEmpty()) {
			String message = "Empty borrower list";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		return borrowerList;
	}

	@Override
	public Borrower getBorrowerDetails(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		if (!LibraryCatalogApiServiceUtil.isValidId(borrowerId)) {
			String message = "Invalid borrower ID [" + borrowerId + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		Borrower borrower = null;

		// Read from cache.
		Map<String, Borrower> borrowerMap = libraryCatalogApiCache.getBorrowerMap();
		if (borrowerMap.containsKey(borrowerId)) {
			logger.info("borrower ID [" + borrowerId + "] found in cache");
			borrower = borrowerMap.get(borrowerId);
			return borrower;
		}

		// Not found in cache. Read from main source.
		try {
			borrower = libraryCatalogApiDataService.getBorrowerDetails(borrowerId);
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}

		if (borrower == null) {
			String message = "Cannot find borrower ID [" + borrowerId + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		} else {
			// Add to cache.
			borrowerMap.put(borrowerId, borrower);
		}

		return borrower;
	}

	@Override
	public String addBorrower(Borrower borrower)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		if (StringUtils.isBlank(borrower.getBorrowerName())) {
			String message = "Invalid borrower name [" + borrower.getBorrowerName() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		// Check if borrower already exists from the main source.
		if (borrowerExists(borrower)) {
			String message = "Borrower already exists [" + borrower.getBorrowerName() + "]";
			logger.error(message);
			throw new LibraryCatalogApiServiceValidationException(message);
		}

		try {
			String newBorrowerId = libraryCatalogApiDataService.addBorrower(borrower);

			if (newBorrowerId == null) {
				String message = "Unable to add borrower [" + borrower.getBorrowerName() + "]";
				logger.error(message);
				throw new LibraryCatalogApiServiceValidationException(message);
			}

			borrower.setBorrowerId(newBorrowerId);

			// Update cache.
			Map<String, Borrower> borrowerMap = libraryCatalogApiCache.getBorrowerMap();
			borrowerMap.put(newBorrowerId, borrower);

			return newBorrowerId;
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw new LibraryCatalogApiServiceException("Internal error");
		}
	}

	/**
	 * This checks if the borrower exists by checking if it has the same name from
	 * one of the borrowers from the main source.
	 * 
	 * @param borrower Borrower instance
	 * @return boolean
	 */
	private boolean borrowerExists(Borrower borrower)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException {
		List<Borrower> borrowerList = getBorrowerList();
		for (Borrower currentBorrower : borrowerList) {
			if (LibraryCatalogApiServiceUtil.isSame(currentBorrower.getBorrowerName(), borrower.getBorrowerName()))
				return true;
		}
		return false;
	}
}
