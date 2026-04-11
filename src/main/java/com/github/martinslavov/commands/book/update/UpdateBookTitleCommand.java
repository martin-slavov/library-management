package com.github.martinslavov.commands.book.update;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;

public class UpdateBookTitleCommand implements Command {

    private final AppContext context;
    private final Book book;

    public UpdateBookTitleCommand(AppContext context, Book book) {
        this.context = context;
        this.book = book;
    }

    @Override
    public void execute() {
        String title = context.input.readString("Enter new title: ");
        book.setTitle(title);
        try {
            context.bookDAO.update(book);
            System.out.println("Title updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
