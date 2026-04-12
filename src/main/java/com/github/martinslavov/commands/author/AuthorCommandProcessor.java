package com.github.martinslavov.commands.author;

import com.github.martinslavov.commands.author.search.*;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class AuthorCommandProcessor {

    public static final int ADD_AUTHOR = 1;
    public static final int REMOVE_AUTHOR = 2;
    public static final int SEARCH_AUTHOR = 3;
    public static final int UPDATE_AUTHOR = 4;
    public static final int BACK = 0;

    private final AppContext context;

    public AuthorCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.AUTHOR_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ADD_AUTHOR -> new AddAuthorCommand(context).execute();
                case REMOVE_AUTHOR -> new RemoveAuthorCommand(context).execute();
                case SEARCH_AUTHOR -> new AuthorSearchCommandProcessor(context).run();
                case UPDATE_AUTHOR -> new UpdateAuthorCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
