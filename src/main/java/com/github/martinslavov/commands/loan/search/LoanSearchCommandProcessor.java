package com.github.martinslavov.commands.loan.search;

import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class LoanSearchCommandProcessor {

    private static final int ALL_LOANS = 1;
    private static final int BY_BOOK = 2;
    private static final int BY_MEMBER = 3;
    private static final int BY_STATUS = 4;
    private static final int BACK = 0;

    private final AppContext context;

    public LoanSearchCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.LOAN_SEARCH_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ALL_LOANS -> new SearchAllLoansCommand(context).execute();
                case BY_BOOK -> new SearchLoanByBookCommand(context).execute();
                case BY_MEMBER -> new SearchLoanByMemberCommand(context).execute();
                case BY_STATUS -> new SearchLoanByStatusCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
