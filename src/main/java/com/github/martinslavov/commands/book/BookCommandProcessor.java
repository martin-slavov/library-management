package com.github.martinslavov.commands.book;

import com.github.martinslavov.commands.book.search.BookSearchCommandProcessor;
import com.github.martinslavov.commands.book.update.BookUpdateCommandProcessor;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class BookCommandProcessor {

    private static final int ADD_BOOK = 1;
    private static final int REMOVE_BOOK = 2;
    private static final int SEARCH_BOOK = 3;
    private static final int UPDATE_BOOK = 4;
    private static final int BACK = 0;

    private final AppContext context;

    public BookCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.BOOKS_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ADD_BOOK-> new AddBookCommand(context).execute();
                case REMOVE_BOOK-> new RemoveBookCommand(context).execute();
                case SEARCH_BOOK-> new BookSearchCommandProcessor(context).run();
                case UPDATE_BOOK-> new BookUpdateCommandProcessor(context).run();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
