package com.librarycatalog.api.exception;

public class LibraryCatalogApiServiceValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * No-argument constructor
	 */
	public LibraryCatalogApiServiceValidationException() {
		super();
	}

	/**
	 * 
	 * @param errorMessage contains some details about the error
	 */
	public LibraryCatalogApiServiceValidationException(String errorMessage) {
		super(errorMessage);
	}
}
