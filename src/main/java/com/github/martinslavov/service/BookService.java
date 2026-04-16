package com.github.martinslavov.service;

import com.github.martinslavov.dao.AuthorDAO;
import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.dao.CategoryDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Book;

import java.util.regex.Pattern;

public class BookService {

    private static final Pattern ISBN_PATTERN = Pattern.compile("^97[89]-?(?:[0-9]-?){9}[0-9]$");

    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final CategoryDAO categoryDAO;

    public BookService(BookDAO bookDAO, AuthorDAO authorDAO, CategoryDAO categoryDAO) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.categoryDAO = categoryDAO;
    }

    public void addBook(Book book) {

        String cleanIsbn = book.getIsbn().replaceAll("-", "");
        book.setIsbn(cleanIsbn);
        if (!ISBN_PATTERN.matcher(book.getIsbn()).matches()) {
            throw new LibraryException("Invalid ISBN format");
        }

        if (bookDAO.findByIsbn(book.getIsbn()).isPresent()) {
            throw new LibraryException("Book with this ISBN already exists");
        }

        if (authorDAO.findById(book.getAuthorId()).isEmpty()) {
            throw new LibraryException("Author not found");
        }

        if (categoryDAO.findById(book.getCategoryId()).isEmpty()) {
            throw new LibraryException("Category not found");
        }

        if (book.getTotalCopies() <= 0 || book.getAvailableCopies() <= 0) {
            throw new LibraryException("Total and Available copies should be greater then 0");
        }

        bookDAO.save(book);
    }

    public void removeBook(Book book) {

        if (bookDAO.findById(book.getBookId()).isEmpty()) {
            throw new LibraryException("Book not found");
        }

        if (book.getAvailableCopies() != book.getTotalCopies()) {
            throw new LibraryException("Cannot remove book — some copies are currently on loan");
        }

        bookDAO.delete(book.getBookId());
    }
}
