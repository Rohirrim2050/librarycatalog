package com.librarycatalog.api.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("libraryCatalogApiIDGeneratorService")
public class LibraryCatalogApiIDGeneratorServiceImpl implements LibraryCatalogApiIDGeneratorService {

	public String generateBookId() {
		return "BK" + generateId();
	}

	public String generateBorrowerId() {
		return "BR" + generateId();
	}

	@Override
	public String generateTransactionId() {
		return "TR" + generateId();
	}

	private String generateId() {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		return formatter.format(currentDate);
	}
}
