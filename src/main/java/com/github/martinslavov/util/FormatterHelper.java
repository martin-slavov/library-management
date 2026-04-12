package com.github.martinslavov.util;

import com.github.martinslavov.model.*;

import java.time.format.DateTimeFormatter;

public class FormatterHelper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

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
                member.getStartDate().format(DATE_FORMAT),
                member.getExpireDate().format(DATE_FORMAT),
                member.getStatus());
    }

    public String formatAuthor(Author author) {
        return String.format("ID: %d | %s %s | %s",
                author.getAuthorId(),
                author.getFirstName(),
                author.getLastName(),
                author.getNationality() != null ? author.getNationality() : "Unknown");
    }

    public String formatCategory(Category category) {
        return String.format("ID: %d | %s | %s",
                category.getCategoryId(),
                category.getName(),
                category.getDescription());
    }

    public String formatLoan(Loan loan) {
        String bookTitle = context.bookDAO.findById(loan.getBookId())
                .map(Book::getTitle)
                .orElse("Unknown");

        String memberName = context.memberDAO.findById(loan.getMemberId())
                .map(member -> member.getFirstName() + " " + member.getLastName())
                .orElse("Unknown");
        return String.format("ID: %d | %s | %s | Start: %s | Due: %s | Returned: %s | Status: %s",
                loan.getLoanId(),
                bookTitle,
                memberName,
                loan.getStartDate().format(DATE_FORMAT),
                loan.getEndDate().format(DATE_FORMAT),
                loan.getReturnDate() != null ? loan.getReturnDate().format(DATE_FORMAT) : "Not returned",
                loan.getStatus());

    }

}
