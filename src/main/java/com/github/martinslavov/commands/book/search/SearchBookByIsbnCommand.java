package com.github.martinslavov.commands.book.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;

import java.util.Optional;

public class SearchBookByIsbnCommand implements Command {

    private final AppContext context;

    public SearchBookByIsbnCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Book By ISBN ===");
        String isbn = context.input.readString("Enter ISBN: ");

        Optional<Book> book = context.bookDAO.findByIsbn(isbn);
        if (book.isEmpty()) {
            System.out.println("Book not found.");
            return;
        }
        System.out.println(context.formatter.formatBook(book.get()));
    }
}
