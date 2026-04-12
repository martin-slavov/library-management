package com.github.martinslavov.commands.category.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAllCategoriesCommand implements Command {

    private final AppContext context;

    public SearchAllCategoriesCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== All Categories ===");
        List<Category> categories = context.categoryDAO.findAll();
        if (categories.isEmpty()) {
            System.out.println("No categories found");
            return;
        }
        categories.forEach(category -> System.out.println(context.formatter.formatCategory(category)));
    }
}
