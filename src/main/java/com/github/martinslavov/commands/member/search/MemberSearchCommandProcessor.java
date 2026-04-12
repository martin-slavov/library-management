package com.github.martinslavov.commands.member.search;

import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class MemberSearchCommandProcessor {

    private static final int ALL_MEMBERS = 1;
    private static final int BY_FIRST_NAME = 2;
    private static final int BY_LAST_NAME = 3;
    private static final int BY_PHONE = 4;
    private static final int BY_EMAIL = 5;
    private static final int BY_STATUS = 6;
    private static final int BACK = 0;

    private final AppContext context;

    public MemberSearchCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.MEMBER_SEARCH_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case ALL_MEMBERS -> new SearchAllMembersCommand(context).execute();
                case BY_FIRST_NAME -> new SearchMemberByFirstNameCommand(context).execute();
                case BY_LAST_NAME -> new SearchMemberByLastNameCommand(context).execute();
                case BY_PHONE -> new SearchMemberByPhoneCommand(context).execute();
                case BY_EMAIL -> new SearchMemberByEmailCommand(context).execute();
                case BY_STATUS -> new SearchMemberByStatusCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
