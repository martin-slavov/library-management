package com.github.martinslavov.commands.book.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchBookByCategoryCommand implements Command {

    private final AppContext context;

    public SearchBookByCategoryCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Book By Category ===");
        Category selectedCategory = context.selector.selectCategory(false);
        if (selectedCategory == null) return;

        List<Book> books = context.bookDAO.findByCategoryId(selectedCategory.getCategoryId());
        if (books.isEmpty()) {
            System.out.println("No books found for this category.");
            return;
        }
        books.forEach(book -> System.out.println(context.formatter.formatBook(book)));
    }
}
