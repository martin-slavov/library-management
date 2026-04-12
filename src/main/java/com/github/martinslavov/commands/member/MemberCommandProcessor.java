package com.github.martinslavov.commands.member;

import com.github.martinslavov.commands.member.search.*;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class MemberCommandProcessor {


    public static final int REGISTER_MEMBER = 1;
    public static final int SUSPEND_MEMBER = 2;
    public static final int RENEW_MEMBERSHIP = 3;
    public static final int SEARCH_MEMBER = 4;
    public static final int UPDATE_MEMBER = 5;
    public static final int BACK = 0;

    private final AppContext context;

    public MemberCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.MEMBER_MENU);
            int choice = context.input.readInt("");
            switch (choice) {
                case REGISTER_MEMBER -> new RegisterMemberCommand(context).execute();
                case SUSPEND_MEMBER -> new SuspendMemberCommand(context).execute();
                case RENEW_MEMBERSHIP -> new RenewMembershipCommand(context).execute();
                case SEARCH_MEMBER -> new MemberSearchCommandProcessor(context).run();
                case UPDATE_MEMBER -> new UpdateMemberCommand(context).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
