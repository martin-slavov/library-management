package com.github.martinslavov.commands.book.update;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;

public class UpdateBookAuthorCommand implements Command {

    private final AppContext context;
    private final Book book;

    public UpdateBookAuthorCommand(AppContext context, Book book) {
        this.context = context;
        this.book = book;
    }

    @Override
    public void execute() {
        Author selectedAuthor = context.selector.selectAuthor(false);
        if (selectedAuthor == null) {
            System.out.println("Author not found.");
            return;
        }

        book.setAuthorId(selectedAuthor.getAuthorId());
        try {
            context.bookDAO.update(book);
            System.out.println("Author updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
