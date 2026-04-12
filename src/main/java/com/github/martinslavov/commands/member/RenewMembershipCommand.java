package com.github.martinslavov.commands.member;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.time.LocalDate;

public class RenewMembershipCommand implements Command {

    private final AppContext context;

    public RenewMembershipCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        Member member = context.selector.selectMember();
        if (member == null) {
            System.out.println("Member not found");
            return;
        }
        LocalDate newExpireDate = context.input.readDate("Enter new expire date:");

        try {
            context.memberService.renewMembership(member, newExpireDate);
            System.out.println("Membership renewed successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
