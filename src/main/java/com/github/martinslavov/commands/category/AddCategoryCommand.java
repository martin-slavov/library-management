package com.github.martinslavov.commands.category;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.util.AppContext;

public class AddCategoryCommand implements Command {

    private final AppContext context;

    public AddCategoryCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Add Category ===");
        String name = context.input.readString("Enter name: ");
        String description = context.input.readString("Enter description: ");

        Category category = new Category(name, description);
        try {
            context.categoryService.addCategory(category);
            System.out.println("Category added successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
