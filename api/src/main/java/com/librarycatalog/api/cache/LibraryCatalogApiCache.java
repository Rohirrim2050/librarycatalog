package com.librarycatalog.api.cache;

import java.util.List;
import java.util.Map;

import com.librarycatalog.api.model.Book;
import com.librarycatalog.api.model.Borrower;
import com.librarycatalog.api.model.BorrowerRecord;

public interface LibraryCatalogApiCache {

	Map<String, Book> getBookMap();

	Map<String, Borrower> getBorrowerMap();

	Map<String, List<BorrowerRecord>> getBorrowerRecordMap();
}
