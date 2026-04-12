package com.github.martinslavov.commands;

import com.github.martinslavov.commands.author.AuthorCommandProcessor;
import com.github.martinslavov.commands.book.BookCommandProcessor;
import com.github.martinslavov.commands.category.CategoryCommandProcessor;
import com.github.martinslavov.commands.fine.FineCommandProcessor;
import com.github.martinslavov.commands.loan.LoanCommandProcessor;
import com.github.martinslavov.commands.member.MemberCommandProcessor;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class CommandProcessor {

    public static final int BOOKS = 1;
    public static final int MEMBERS = 2;
    public static final int AUTHORS = 3;
    public static final int CATEGORIES = 4;
    public static final int LOANS = 5;
    public static final int FINES = 6;
    public static final int QUIT = 0;

    private final AppContext context;

    public CommandProcessor(AppContext context) {
        this.context = context;
    }

    public void process() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.MAIN_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case BOOKS -> new BookCommandProcessor(context).run();
                case MEMBERS -> new MemberCommandProcessor(context).run();
                case AUTHORS -> new AuthorCommandProcessor(context).run();
                case CATEGORIES -> new CategoryCommandProcessor(context).run();
                case LOANS -> new LoanCommandProcessor(context).run();
                case FINES -> new FineCommandProcessor(context).run();
                case QUIT -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
