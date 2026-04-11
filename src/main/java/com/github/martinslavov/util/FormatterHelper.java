package com.github.martinslavov.util;

import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;

public class FormatterHelper {

    private final AppContext context;

    public FormatterHelper(AppContext context) {
        this.context = context;
    }

    public String formatBook(Book book) {
        String authorName = context.authorDAO.findById(book.getAuthorId())
                .map(a -> a.getFirstName() + " " + a.getLastName())
                .orElse("Unknown");

        String categoryName = context.categoryDAO.findById(book.getCategoryId())
                .map(Category::getName)
                .orElse("Unknown");

        return String.format("ID: %d | %s | %s | %s | %d | Copies: %d/%d",
                book.getBookId(), book.getTitle(), authorName, categoryName,
                book.getPublicationDate(), book.getAvailableCopies(), book.getTotalCopies());
    }
}
