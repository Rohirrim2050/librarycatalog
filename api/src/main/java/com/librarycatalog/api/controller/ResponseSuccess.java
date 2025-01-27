package com.librarycatalog.api.controller;

/**
 * Success response for controller.
 */
public class ResponseSuccess implements Response {
	private Object data;
	private String message = "";

	public ResponseSuccess() {
	}

	public ResponseSuccess(Object data) {
		this.data = data;
	}

	public ResponseSuccess(Object data, String message) {
		this.data = data;
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public String getMessage() {
		return message;
	}
}
