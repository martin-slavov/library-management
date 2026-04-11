package com.github.martinslavov.commands.book.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchBookByAuthorCommand implements Command {

    private final AppContext context;

    public SearchBookByAuthorCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Book By Author ===");
        Author selectedAuthor = context.selector.selectAuthor(false);
        if (selectedAuthor == null) return;

        List<Book> books = context.bookDAO.findByAuthorId(selectedAuthor.getAuthorId());
        if (books.isEmpty()) {
            System.out.println("No books found for this author.");
            return;
        }
        books.forEach(book -> System.out.println(context.formatter.formatBook(book)));
    }
}
