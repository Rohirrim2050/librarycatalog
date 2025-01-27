package com.librarycatalog.api.model;

public class BorrowerRecord {
	private String transactionId;
	private String borrowerId;
	private String bookId;
	private String borrowDate;
	private String returnDate;

	public BorrowerRecord() {
	}

	public BorrowerRecord(String transactionId, String borrowerId, String bookId, String borrowDate,
			String returnDate) {
		this.transactionId = transactionId;
		this.borrowerId = borrowerId;
		this.bookId = bookId;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(String borrowDate) {
		this.borrowDate = borrowDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
}