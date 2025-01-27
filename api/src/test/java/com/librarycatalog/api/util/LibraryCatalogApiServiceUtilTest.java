package com.librarycatalog.api.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LibraryCatalogApiServiceUtilTest {
	@Test
	public void idIsNull() {
		assertFalse(LibraryCatalogApiServiceUtil.isValidId(null));
	}

	@Test
	public void idIsEmpty() {
		assertFalse(LibraryCatalogApiServiceUtil.isValidId(""));
	}

	@Test
	public void idIsAlphaNumeric() {
		assertTrue(LibraryCatalogApiServiceUtil.isValidId("as12"));
	}

	@Test
	public void idHasSpecialCharacters() {
		assertFalse(LibraryCatalogApiServiceUtil.isValidId("as12$"));
	}

	@Test
	public void compareStrings() {
		assertTrue(LibraryCatalogApiServiceUtil.isSame("", ""));
		assertTrue(LibraryCatalogApiServiceUtil.isSame(null, null));
		assertTrue(LibraryCatalogApiServiceUtil.isSame(null, ""));
		assertTrue(LibraryCatalogApiServiceUtil.isSame("", null));
		assertTrue(LibraryCatalogApiServiceUtil.isSame("abc", "ABC"));
		assertTrue(LibraryCatalogApiServiceUtil.isSame("The Fellowship of the Ring",
				"The     Fellowship    of the   Ring"));
		assertFalse(LibraryCatalogApiServiceUtil.isSame("The Fellowship of the Ring",
				"The  One   Fellowship    of the   Ring"));
	}

	@Test
	public void compareDateStrings() {
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1("", ""));
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1("", "asddf"));
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1("asdf", ""));
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1(null, null));
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1(null, ""));
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1("", null));
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1("02012025", "01012025"));
		assertFalse(LibraryCatalogApiServiceUtil.isDate2AfterDate1("02012025", "02012025"));
		assertTrue(LibraryCatalogApiServiceUtil.isDate2AfterDate1("02012025", "03012025"));
	}
}
