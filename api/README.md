# Library Catalog API

* [Instructions](#instructions)
* [Assumptions](#assumptions)


## Instructions

- Use any IDE and go to this repository **https://github.com/Rohirrim2050/librarycatalog** to clone it.
- Check this location on your local machine. Make sure there are three text files, **books**, **borrowers** and **borrowers_records**.  The borrowers_records will be an empty file.
 
```
../api/target/classes/data
```
- Locate this file in your IDE and right-click **Run As** then **Java Application**.

```
../api/src/main/java/com/librarycatalog/api/LibraryCatalogApiApplication.java
```

- Locate this folder in your IDE and right-click **Run As** then **JUnit Test**. Run the test from the folder level. If there is a failure in one of the tests, go to the data files stated above and remove the newly-added lines.  Then, run the tests again.

```
../api/src/test/java/com/librarycatalog/api/controller
```


## Assumptions

### General
- Request parameter, instead of path variable, is used on any endpoint that requires an ID.
- All IDs are alpha-numeric.
- Files are used for storage to simplify setup.
- Comparison for ID is case-sensitive. It can be improved to be case-insensitive.
- Date is in MMddyyyy format.
- A transaction ID is added when storing borrower record.

### Book
- The book title can be any text.
- The book author can be any text and is only stored in one field.
- When retrieving details of a book, the result will contain the ID, title and author.
- When retrieving list of all books, the result will contain the ID, title and author for each book.
- A book entry, with the same title and author with extra spaces in between, cannot be added if a book with same title and author, without extra spaces, already exists.
- It is possible to return an empty book list.

### Borrower
- The borrower name can be any text and is only stored in one field. A person, in first name-last name order, will be different from another person, with same last name-first name order.
- When retrieving details of a borrower, the result will contain the ID and borrower name.
- When retrieving list of all borrowers, the result will contain the ID and borrower name for each borrower.
- A borrower entry, with the same name with extra spaces in between, cannot be added if a borrower with same name, without extra spaces, already exists.
- It is possible to return an empty borrower list.

### Borrowing
- The borrow date can be any date.  It is not set as the current date when the api is called.
- When storing a borrowing record when the borrower borrows a book, the return date is saved as **<noReturnDate>**.
- When returning the book, the **<noReturnDate>** is replaced with the correct return date.
- A return date is only valid if it's after the borrow date. It can be any date in the future.
