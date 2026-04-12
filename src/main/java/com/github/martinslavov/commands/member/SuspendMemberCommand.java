package com.github.martinslavov.commands.member;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

public class SuspendMemberCommand implements Command {

    private final AppContext context;

    public SuspendMemberCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        Member member = context.selector.selectMember();
        if (member == null) {
            System.out.println("Member not found");
            return;
        }
        try {
            context.memberService.suspendMember(member);
            System.out.println("Member suspended successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
