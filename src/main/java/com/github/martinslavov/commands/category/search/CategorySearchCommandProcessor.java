package com.github.martinslavov.commands.category.search;

import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class CategorySearchCommandProcessor {

    public static final int ALL_CATEGORIES = 1;
    public static final int BY_NAME = 2;
    public static final int BACK = 0;

    private final AppContext context;

    public CategorySearchCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.CATEGORY_SEARCH_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ALL_CATEGORIES -> new SearchAllCategoriesCommand(context).execute();
                case BY_NAME -> new SearchCategoryByNameCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
