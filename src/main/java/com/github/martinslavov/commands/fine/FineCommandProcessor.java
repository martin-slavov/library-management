package com.github.martinslavov.commands.fine;

import com.github.martinslavov.commands.fine.search.FineSearchCommandProcessor;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class FineCommandProcessor {

    public static final int PAY_FINE = 1;
    public static final int WAIVE_FINE = 2;
    public static final int SEARCH_FINE = 3;
    public static final int BACK = 0;

    private final AppContext context;

    public FineCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.FINE_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case PAY_FINE -> new PayFineCommand(context).execute();
                case WAIVE_FINE -> new WaiveFineCommand(context).execute();
                case SEARCH_FINE -> new FineSearchCommandProcessor(context).run();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
