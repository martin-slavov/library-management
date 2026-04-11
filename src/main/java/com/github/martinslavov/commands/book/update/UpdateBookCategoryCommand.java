package com.github.martinslavov.commands.book.update;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.util.AppContext;

public class UpdateBookCategoryCommand implements Command {

    private final AppContext context;
    private final Book book;

    public UpdateBookCategoryCommand(AppContext context, Book book) {
        this.context = context;
        this.book = book;
    }

    @Override
    public void execute() {
        Category selectedCategory = context.selector.selectCategory(false);
        if (selectedCategory == null) {
            System.out.println("Category not found.");
            return;
        }

        book.setCategoryId(selectedCategory.getCategoryId());
        try {
            context.bookDAO.update(book);
            System.out.println("Category updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
