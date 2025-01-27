package com.librarycatalog.api.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 * This file data handler reads and updates files, located in the classpath.
 */
@Component
@Qualifier("libraryCatalogApiFileDataHandler")
public class LibraryCatalogApiFileDataHandlerImpl implements LibraryCatalogApiFileDataHandler {

	private static final Logger logger = LogManager.getLogger(LibraryCatalogApiFileDataHandlerImpl.class);

	@Override
	public List<String> getDataList(String fileName) throws IOException {
		List<String> dataList = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(
				new FileReader(ResourceUtils.getFile("classpath:" + fileName)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				dataList.add(line);
			}
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw ioEx;
		}

		logger.info("No error in reading data file");
		return dataList;
	}

	@Override
	public void addNewEntry(String data, String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(ResourceUtils.getFile("classpath:" + fileName), true))) {
			writer.newLine();
			writer.write(data);
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
			throw ioEx;
		}

		logger.info("No error in inserting data to file");
	}

	@Override
	public void clearFileContent(String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(ResourceUtils.getFile("classpath:" + fileName), false))) {
			writer.write("");
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
		}

		logger.info("No error in clearing the file");
	}

	@Override
	public void saveDataList(List<String> dataList, String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(ResourceUtils.getFile("classpath:" + fileName), false))) {
			for (String line : dataList) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException ioEx) {
			logger.error(ioEx.getMessage());
		}

		logger.info("No error in saving data to file");
	}
}
