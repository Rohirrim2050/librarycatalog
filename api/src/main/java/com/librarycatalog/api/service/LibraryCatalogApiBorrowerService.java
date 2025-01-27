package com.librarycatalog.api.service;

import java.util.List;

import com.librarycatalog.api.exception.LibraryCatalogApiServiceException;
import com.librarycatalog.api.exception.LibraryCatalogApiServiceValidationException;
import com.librarycatalog.api.model.Borrower;

public interface LibraryCatalogApiBorrowerService {

	List<Borrower> getBorrowerList()
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	Borrower getBorrowerDetails(String borrowerId)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;

	String addBorrower(Borrower borrower)
			throws LibraryCatalogApiServiceException, LibraryCatalogApiServiceValidationException;
}
