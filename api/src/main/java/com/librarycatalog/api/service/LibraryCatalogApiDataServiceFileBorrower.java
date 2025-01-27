package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.List;

import com.librarycatalog.api.model.Borrower;

public interface LibraryCatalogApiDataServiceFileBorrower {

	List<Borrower> getBorrowerList() throws IOException;

	Borrower getBorrowerDetails(String borrowerId) throws IOException;

	String addBorrower(Borrower borrower) throws IOException;
}
