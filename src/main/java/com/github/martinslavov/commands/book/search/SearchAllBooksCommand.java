package com.github.martinslavov.commands.book.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAllBooksCommand implements Command {

    private final AppContext context;

    public SearchAllBooksCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        List<Book> books = context.bookDAO.findAll();
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        books.forEach(book -> System.out.println(context.formatter.formatBook(book)));
    }
}
