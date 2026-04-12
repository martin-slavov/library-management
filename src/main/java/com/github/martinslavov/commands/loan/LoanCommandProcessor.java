package com.github.martinslavov.commands.loan;

import com.github.martinslavov.commands.loan.search.LoanSearchCommandProcessor;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class LoanCommandProcessor {

    private static final int BORROW_BOOK = 1;
    private static final int RETURN_BOOK = 2;
    private static final int GET_MEMBER_LOAN_HISTORY = 3;
    private static final int SEARCH_LOAN = 4;
    private static final int BACK = 0;

    private final AppContext context;

    public LoanCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.LOAN_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case BORROW_BOOK -> new BorrowBookCommand(context).execute();
                case RETURN_BOOK -> new ReturnBookCommand(context).execute();
                case GET_MEMBER_LOAN_HISTORY -> new GetMemberLoanHistoryCommand(context).execute();
                case SEARCH_LOAN -> new LoanSearchCommandProcessor(context).run();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
