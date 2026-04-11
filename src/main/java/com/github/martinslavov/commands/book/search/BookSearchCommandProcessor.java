package com.github.martinslavov.commands.book.search;

import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class BookSearchCommandProcessor {

    private static final int ALL_BOOKS = 1;
    private static final int BY_ISBN = 2;
    private static final int BY_TITLE = 3;
    private static final int BY_AUTHOR = 4;
    private static final int BY_CATEGORY = 5;
    private static final int BACK = 0;

    private final AppContext context;

    public BookSearchCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.BOOKS_SEARCH_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ALL_BOOKS -> new SearchAllBooksCommand(context).execute();
                case BY_ISBN -> new SearchBookByIsbnCommand(context).execute();
                case BY_TITLE -> new SearchBookByTitleCommand(context).execute();
                case BY_AUTHOR -> new SearchBookByAuthorCommand(context).execute();
                case BY_CATEGORY -> new SearchBookByCategoryCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
