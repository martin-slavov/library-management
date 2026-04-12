package com.github.martinslavov.commands.author.search;

import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class AuthorSearchCommandProcessor {

    public static final int ALL_AUTHORS = 1;
    public static final int BY_FIRST_NAME = 2;
    public static final int BY_LAST_NAME = 3;
    public static final int BY_FULL_NAME = 4;
    public static final int BY_NATIONALITY = 5;
    public static final int BACK = 0;

    private final AppContext context;

    public AuthorSearchCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.AUTHOR_SEARCH_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ALL_AUTHORS -> new SearchAllAuthorsCommand(context).execute();
                case BY_FIRST_NAME -> new SearchAuthorByFirstNameCommand(context).execute();
                case BY_LAST_NAME -> new SearchAuthorByLastNameCommand(context).execute();
                case BY_FULL_NAME -> new SearchAuthorByFullNameCommand(context).execute();
                case BY_NATIONALITY -> new SearchAuthorByNationalityCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
