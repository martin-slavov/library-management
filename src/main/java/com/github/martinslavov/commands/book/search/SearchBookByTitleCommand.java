package com.github.martinslavov.commands.book.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchBookByTitleCommand implements Command {

    private final AppContext context;

    public SearchBookByTitleCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Book By Title ===");
        String title = context.input.readString("Enter title: ");

        List<Book> books = context.bookDAO.findByTitle(title);
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        books.forEach(book -> System.out.println(context.formatter.formatBook(book)));
    }
}
