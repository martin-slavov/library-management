package com.github.martinslavov.commands.book.update;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;

public class AddBookCopiesCommand implements Command {

    private final AppContext context;
    private final Book book;

    public AddBookCopiesCommand(AppContext context, Book book) {
        this.context = context;
        this.book = book;
    }

    @Override
    public void execute() {
        int copies = context.input.readInt("Enter number of copies to add: ");

        if (copies <= 0) {
            System.err.println("Number of copies must be greater than 0");
            return;
        }
        book.setTotalCopies(book.getTotalCopies() + copies);
        book.setAvailableCopies(book.getAvailableCopies() + copies);
        try {
            context.bookDAO.update(book);
            System.out.println("Copies added successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
