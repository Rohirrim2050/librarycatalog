package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.List;

import com.librarycatalog.api.model.BorrowerRecord;

public interface LibraryCatalogApiDataServiceFileBorrowerRecord {

	/**
	 * This retrieves the data string list, by the borrower ID, and converts into
	 * BorrowerRecords instances. Only BorrowerRecord instances, without the return
	 * date, will be included.
	 * 
	 * @param borrowerId Borrower ID
	 * @return List<BorrowerRecord> instance
	 * @throws IOException
	 */
	List<BorrowerRecord> getBorrowerRecordUnreturnedList(String borrowerId) throws IOException;

	/**
	 * This retrieves the data string list and converts into BorrowerRecords
	 * instances. Only BorrowerRecord instances, without the return date, will be
	 * included.
	 * 
	 * @param borrowerId Borrower ID
	 * @return List<BorrowerRecord> instance
	 * @throws IOException
	 */
	List<BorrowerRecord> getBorrowerRecordUnreturnedList() throws IOException;

	/**
	 * This adds a new entry into the BorrowerRecord file.
	 * 
	 * @param borrowerRecord new BorrowerRecord entry
	 * @return transaction ID
	 * @throws IOException
	 */
	String addBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException;

	/**
	 * This updates the existing BorrowerRecord instance.
	 * 
	 * @param borrowerRecord BorrowerRecord to update
	 * @return transaction ID from borrowing
	 * @throws IOException
	 */
	String updateBorrowerRecord(BorrowerRecord borrowerRecord) throws IOException;
}