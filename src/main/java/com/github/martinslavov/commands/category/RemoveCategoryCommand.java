package com.github.martinslavov.commands.category;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.util.AppContext;

public class RemoveCategoryCommand implements Command {

    private final AppContext context;

    public RemoveCategoryCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Remove Category ===");
        Category category = context.selector.selectCategory(false);
        if (category == null) return;

        if (!context.input.readConfirmation("Are you sure you want to delete category '" + category.getName() + "'")) {
            System.out.println("Category not deleted.");
            return;
        }

        try {
            context.categoryService.removeCategory(category);
            System.out.println("Category removed successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
