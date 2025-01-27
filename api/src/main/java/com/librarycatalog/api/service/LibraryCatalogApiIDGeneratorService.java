package com.librarycatalog.api.service;

public interface LibraryCatalogApiIDGeneratorService {
	String generateBookId();

	String generateBorrowerId();
	
	String generateTransactionId();
}
