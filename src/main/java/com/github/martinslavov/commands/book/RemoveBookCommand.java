package com.github.martinslavov.commands.book;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.util.AppContext;

public class RemoveBookCommand implements Command {

    private final AppContext context;

    public RemoveBookCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Remove book ===");
        String isbn = context.input.readString("Enter ISBN: ");

        var book = context.bookDAO.findByIsbn(isbn);
        if (book.isEmpty()) {
            System.out.println("Book not found");
            return;
        }
        if (context.input.readConfirmation(String.format("Are you sure you want to delete book with ISBN /%s/", isbn))) {
            try {
                context.bookService.removeBook(book.get());
                System.out.println("Book deleted successfully!");
            } catch (LibraryException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Book not deleted!");
        }
    }
}
