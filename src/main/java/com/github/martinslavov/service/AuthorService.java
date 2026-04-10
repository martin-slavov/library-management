package com.github.martinslavov.service;

import com.github.martinslavov.dao.AuthorDAO;
import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;

public class AuthorService {

    private final AuthorDAO authorDAO;
    private final BookDAO bookDAO;

    public AuthorService(AuthorDAO authorDAO, BookDAO bookDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    public void addAuthor(Author author) {

        if (!authorDAO.findByFullName(author.getFirstName(), author.getLastName()).isEmpty()) {
            throw new LibraryException("Author already exists");
        }
        authorDAO.save(author);
    }

    public void removeAuthor(Author author) {

        if (authorDAO.findById(author.getAuthorId()).isEmpty()) {
            throw new LibraryException("Author not found");
        }

        if (!bookDAO.findByAuthorId(author.getAuthorId()).isEmpty()) {
            throw new LibraryException("Cannot remove author — they have books in the catalog");
        }
        authorDAO.delete(author.getAuthorId());
    }
}
