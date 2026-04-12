package com.github.martinslavov.commands.category;

import com.github.martinslavov.commands.category.search.CategorySearchCommandProcessor;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class CategoryCommandProcessor {

    public static final int ADD_CATEGORY = 1;
    public static final int REMOVE_CATEGORY = 2;
    public static final int SEARCH_CATEGORY = 3;
    public static final int BACK = 0;

    private final AppContext context;

    public CategoryCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.CATEGORY_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ADD_CATEGORY -> new AddCategoryCommand(context).execute();
                case REMOVE_CATEGORY -> new RemoveCategoryCommand(context).execute();
                case SEARCH_CATEGORY -> new CategorySearchCommandProcessor(context).run();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
