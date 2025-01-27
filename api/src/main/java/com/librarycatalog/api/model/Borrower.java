package com.librarycatalog.api.model;

public class Borrower {
	private String borrowerId;
	private String borrowerName;

	public Borrower() {
	}

	public Borrower(String borrowerId, String borrowerName) {
		this.borrowerId = borrowerId;
		this.borrowerName = borrowerName;
	}

	public String getBorrowerId() {
		return borrowerId;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}
}
