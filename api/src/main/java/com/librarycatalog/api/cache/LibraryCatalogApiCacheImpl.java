package com.librarycatalog.api.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;

/**
 * This acts as a temporary cache.
 */
@Component
@Qualifier("libraryCatalogApiCache")
public class LibraryCatalogApiCacheImpl implements LibraryCatalogApiCache {

	private Map<String, Book> bookMap;
	private Map<String, Borrower> borrowerMap;
	private Map<String, List<BorrowerRecord>> brrowerRecordMap;

	public LibraryCatalogApiCacheImpl() {
		bookMap = new HashMap<String, Book>();
		borrowerMap = new HashMap<String, Borrower>();
		brrowerRecordMap = new HashMap<String, List<BorrowerRecord>>();
	}

	@Override
	public Map<String, Book> getBookMap() {
		return bookMap;
	}

	@Override
	public Map<String, Borrower> getBorrowerMap() {
		return borrowerMap;
	}

	@Override
	public Map<String, List<BorrowerRecord>> getBorrowerRecordMap() {
		return brrowerRecordMap;
	}
}
