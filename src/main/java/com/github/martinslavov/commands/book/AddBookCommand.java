package com.github.martinslavov.commands.book;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.util.AppContext;

public class AddBookCommand implements Command {

    private final AppContext context;

    public AddBookCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Add Book ===");

        String isbn = context.input.readString("Enter ISBN: ");
        String title = context.input.readString("Enter title: ");

        Author selectedAuthor = context.selector.selectAuthor(true);
        if (selectedAuthor == null) return;

        Category selectedCategory = context.selector.selectCategory(true);
        if (selectedCategory == null) return;

        int publicationYear = context.input.readInt("Enter publication year: ");
        int totalCopies = context.input.readInt("Enter total copies: ");
        int availableCopies = context.input.readInt("Enter available copies: ");

        Book book = new Book(isbn, title, selectedAuthor.getAuthorId(),
                selectedCategory.getCategoryId(), publicationYear,
                totalCopies, availableCopies);
        try {
            context.bookService.addBook(book);
            System.out.println("Book added successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
