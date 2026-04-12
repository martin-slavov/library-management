package com.github.martinslavov.commands.fine.search;

import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class FineSearchCommandProcessor {

    public static final int ALL_FINES = 1;
    public static final int BY_STATUS = 2;
    public static final int BACK = 0;

    private final AppContext context;

    public FineSearchCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.FINE_SEARCH_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ALL_FINES -> new SearchAllFinesCommand(context).execute();
                case BY_STATUS -> new SearchFineByStatusCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
