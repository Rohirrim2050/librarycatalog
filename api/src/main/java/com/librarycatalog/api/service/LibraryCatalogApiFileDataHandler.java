package com.librarycatalog.api.service;

import java.io.IOException;
import java.util.List;

public interface LibraryCatalogApiFileDataHandler {

	List<String> getDataList(String filePath) throws IOException;

	void addNewEntry(String data, String fileName) throws IOException;

	void clearFileContent(String fileName) throws IOException;

	void saveDataList(List<String> dataList, String fileName) throws IOException;
}
