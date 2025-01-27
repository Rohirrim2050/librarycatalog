package com.librarycatalog.api.controller;

/**
 * Error response for controller.
 */
public class ResponseError implements Response {
	private String message;

	public ResponseError() {
	}

	public ResponseError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
