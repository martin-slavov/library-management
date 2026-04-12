package com.github.martinslavov.util;

import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.model.Member;

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

    public String formatMember(Member member) {
        return String.format("ID: %d | %s %s | %s | %s | Joined: %s | Expires: %s | Status: %s",
                member.getMemberId(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getPhone() != null ? member.getPhone() : "No phone",
                member.getStartDate(),
                member.getExpireDate(),
                member.getStatus());
    }
}
