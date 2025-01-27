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

import com.librarycatalog.api.model.Borrower;

/**
 * This data service converts file data into business objects.
 */
@Component
@Qualifier("libraryCatalogApiDataServiceFileBorrower")
public class LibraryCatalogApiDataServiceFileBorrowerImpl implements LibraryCatalogApiDataServiceFileBorrower {

	private static final Logger logger = LogManager.getLogger(LibraryCatalogApiDataServiceFileBorrowerImpl.class);

	private final String FILE_BORROWERS = "data/borrowers.txt";

	@Autowired
	@Qualifier("libraryCatalogApiFileDataHandler")
	private LibraryCatalogApiFileDataHandler libraryCatalogApiFileDataHandler;

	@Autowired
	@Qualifier("libraryCatalogApiIDGeneratorService")
	private LibraryCatalogApiIDGeneratorService libraryCatalogApiIDGeneratorService;

	@Override
	public List<Borrower> getBorrowerList() throws IOException {
		List<String> dataList = libraryCatalogApiFileDataHandler.getDataList(FILE_BORROWERS);
		if (dataList.isEmpty()) {
			logger.error("Encountered empty data from file");
			return null;
		}

		List<Borrower> borrowerList = new ArrayList<Borrower>();
		String[] dataArray = null;

		for (String data : dataList) {
			if (StringUtils.isBlank(data)) {
				logger.error("Encountered empty borrower detail");
				continue;
			}
			dataArray = data.split(",");
			if (dataArray.length == 0) {
				logger.error("Encountered empty borrower detail");
			} else if (dataArray.length == 2) {
				borrowerList.add(new Borrower(dataArray[0], dataArray[1]));
			} else {
				logger.error("Issue with borrower ID [" + dataArray[0] + "]");
			}
		}
		return borrowerList;
	}

	@Override
	public Borrower getBorrowerDetails(String borrowerId) throws IOException {
		List<Borrower> borrowerList = getBorrowerList();
		if (borrowerList == null || borrowerList.isEmpty()) {
			logger.error("Encountered borrower list");
			return null;
		}

		for (Borrower borrower : borrowerList) {
			if (borrower.getBorrowerId().equals(borrowerId))
				return borrower;
		}

		return null;
	}

	@Override
	public String addBorrower(Borrower borrower) throws IOException {
		try {
			String newBorrowerId = libraryCatalogApiIDGeneratorService.generateBorrowerId();
			String data = newBorrowerId + "," + borrower.getBorrowerName();
			libraryCatalogApiFileDataHandler.addNewEntry(data, FILE_BORROWERS);
			logger.info("Added new borrower [" + newBorrowerId + "]");
			return newBorrowerId;
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw ioEx;
		}
	}
}
