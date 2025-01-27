package com.librarycatalog.api.exception;

public class LibraryCatalogApiServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * No-argument constructor
	 */
	public LibraryCatalogApiServiceException() {
		super();
	}

	/**
	 * 
	 * @param errorMessage contains some details about the error
	 */
	public LibraryCatalogApiServiceException(String errorMessage) {
		super(errorMessage);
	}
}
