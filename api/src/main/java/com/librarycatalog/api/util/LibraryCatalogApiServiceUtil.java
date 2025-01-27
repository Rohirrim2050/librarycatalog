package com.librarycatalog.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.librarycatalog.api.service.LibraryCatalogApiBorrowedBookService;

public interface LibraryCatalogApiServiceUtil {

	/**
	 * This validates the ID by checking if it's blank or only contains letters and
	 * numbers.
	 * 
	 * @param data ID to validate
	 * @return boolean
	 */
	static boolean isValidId(String data) {
		if (StringUtils.isBlank(data) || !isAlphanumeric(data))
			return false;
		return true;
	}

	/**
	 * This checks if the data only contains letters and and numbers
	 * 
	 * @param data string to validate
	 * @return boolean
	 */
	static boolean isAlphanumeric(String data) {
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
		Matcher matcher = pattern.matcher(data);
		return !matcher.find();
	}

	/**
	 * This compares two strings regardless of the inner spaces.
	 * 
	 * @param value1 first string
	 * @param value2 second string
	 * @return boolean
	 */
	static boolean isSame(String value1, String value2) {
		value1 = StringUtils.trimToEmpty(value1).toLowerCase();
		value2 = StringUtils.trimToEmpty(value2).toLowerCase();

		// Straight-forward comparison
		if (value1.equals(value2))
			return true;

		// Compare letters. They may have extra spaces in between.
		int index1 = 0;
		int index2 = 0;

		while (index1 < value1.length() && index2 < value2.length()) {
			// Skip spaces.
			if (value1.charAt(index1) == ' ') {
				index1++;
				continue;
			} else if (value2.charAt(index2) == ' ') {
				index2++;
				continue;
			}

			if (value1.charAt(index1) != value2.charAt(index2)) {
				return false;
			}

			index1++;
			index2++;
		}

		return true;
	}

	/**
	 * This compares if the first date is before the second date.
	 * @param dateString1 first date
	 * @param dateString2 second date
	 * @return boolean
	 */
	static boolean isDate2AfterDate1(String dateString1, String dateString2) {
		// Invalid dates
		if (StringUtils.isBlank(dateString1) || StringUtils.isBlank(dateString2))
			return false;

		SimpleDateFormat sdf = new SimpleDateFormat(LibraryCatalogApiBorrowedBookService.DATE_FORMAT);

		try {
			Date date1 = sdf.parse(dateString1);
			Date date2 = sdf.parse(dateString2);

			if (date1.compareTo(date2) < 0) {
				return true;
			}
		} catch (ParseException pEx) {
			return false;
		}

		return false;
	}
}
