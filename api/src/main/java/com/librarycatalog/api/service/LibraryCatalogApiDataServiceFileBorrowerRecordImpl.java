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

import com.librarycatalog.api.model.BorrowerRecord;

/**
 * This data service converts file data into business objects.
 */
@Component
@Qualifier("libraryCatalogApiDataServiceFileBorrowerRecord")
public class LibraryCatalogApiDataServiceFileBorrowerRecordImpl
		implements LibraryCatalogApiDataServiceFileBorrowerRecord {

	private static final Logger logger = LogManager.getLogger(LibraryCatalogApiDataServiceFileBorrowerRecordImpl.class);

	private final String FILE_BORROWER_RECORDS = "data/borrowers_records.txt";

	private final String NO_RETURN_DATE = "<noReturnDate>";

	@Autowired
	@Qualifier("libraryCatalogApiFileDataHandler")
	private LibraryCatalogApiFileDataHandler libraryCatalogApiFileDataHandler;

	@Autowired
	@Qualifier("libraryCatalogApiIDGeneratorService")
	private LibraryCatalogApiIDGeneratorService libraryCatalogApiIDGeneratorService;

	@Override
	public List<BorrowerRecord> getBorrowerRecordUnreturnedList(String borrowerId) throws IOException {
		return getBorrowerRecordUnreturnedList(borrowerId, true);
	}

	@Override
	public List<BorrowerRecord> getBorrowerRecordUnreturnedList() throws IOException {
		return getBorrowerRecordUnreturnedList(null, false);
	}

	/**
	 * This retrieves a filtered list of BorrowerRecord instances. Only
	 * BorrowerRecord instances, without the return date, will be included. It can
	 * also filter by borrower ID, if included.
	 * 
	 * @param borrowerId         Borrower ID
	 * @param searchByBorrowerId boolean
	 * @return List<BorrowerRecord>
	 * @throws IOException
	 */
	private List<BorrowerRecord> getBorrowerRecordUnreturnedList(String borrowerId, boolean searchByBorrowerId)
			throws IOException {
		List<BorrowerRecord> borrowerRecordListFiltered = new ArrayList<BorrowerRecord>();

		List<BorrowerRecord> borrowerRecordList = getBorrowerRecordList();

		if (borrowerRecordList == null) {
			logger.error("Encountered empty data from file");
			return null;
		}

		for (BorrowerRecord borrowerRecordElement : borrowerRecordList) {
			if (NO_RETURN_DATE.equals(borrowerRecordElement.getReturnDate())) {
				if (searchByBorrowerId && !borrowerId.equals(borrowerRecordElement.getBorrowerId())) {
					continue;
				}

				borrowerRecordListFiltered.add(borrowerRecordElement);
			}
		}

		return borrowerRecordListFiltered;
	}

	@Override
	public String addBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException {
		try {
			String newTransactionId = libraryCatalogApiIDGeneratorService.generateTransactionId();

			// Make it unique by adding the other IDs.
			newTransactionId += borrowerRecord.getBorrowerId() + borrowerRecord.getBookId();

			String data = newTransactionId + "," + borrowerRecord.getBorrowerId() + "," + borrowerRecord.getBookId()
					+ "," + borrowerRecord.getBorrowDate() + "," + NO_RETURN_DATE;

			libraryCatalogApiFileDataHandler.addNewEntry(data, FILE_BORROWER_RECORDS);
			logger.info("Added new borrower record [" + newTransactionId + "]");
			return newTransactionId;
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw ioEx;
		}
	}

	@Override
	public String updateBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException {
		List<BorrowerRecord> borrowerRecordList = getBorrowerRecordList();

		for (BorrowerRecord borrowerRecordElement : borrowerRecordList) {
			if (borrowerRecordElement.getTransactionId().equals(borrowerRecord.getTransactionId())) {
				borrowerRecordElement.setReturnDate(borrowerRecord.getReturnDate());
				break;
			}
		}

		List<String> borrowerRecordStringList = new ArrayList<String>();
		for (BorrowerRecord borrowerRecordElement : borrowerRecordList) {
			borrowerRecordStringList.add(borrowerRecordElement.getTransactionId() + ","
					+ borrowerRecordElement.getBorrowerId() + "," + borrowerRecordElement.getBookId() + ","
					+ borrowerRecordElement.getBorrowDate() + "," + borrowerRecordElement.getReturnDate());
		}

		libraryCatalogApiFileDataHandler.clearFileContent(FILE_BORROWER_RECORDS);

		libraryCatalogApiFileDataHandler.saveDataList(borrowerRecordStringList, FILE_BORROWER_RECORDS);

		return borrowerRecord.getTransactionId();
	}

	/**
	 * This returns a list of BorrowerRecord instances from the file.
	 * 
	 * @return List<BorrowerRecord> instance
	 * @throws IOException
	 */
	private List<BorrowerRecord> getBorrowerRecordList() throws IOException {
		List<String> dataList = libraryCatalogApiFileDataHandler.getDataList(FILE_BORROWER_RECORDS);
		if (dataList.isEmpty()) {
			logger.error("Encountered empty data from file");
			return null;
		}

		List<BorrowerRecord> borrowerRecordList = new ArrayList<BorrowerRecord>();

		String[] dataArray = null;

		for (String data : dataList) {
			if (StringUtils.isBlank(data)) {
				logger.error("Encountered empty borrower record detail");
				continue;
			}
			dataArray = data.split(",");
			if (dataArray.length == 0) {
				logger.error("Encountered empty borrower record detail");
			} else {
				borrowerRecordList
						.add(new BorrowerRecord(dataArray[0], dataArray[1], dataArray[2], dataArray[3], dataArray[4]));
			}
		}

		return borrowerRecordList;
	}
}
