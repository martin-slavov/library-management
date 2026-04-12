package com.github.martinslavov.commands.member.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.MemberStatus;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchMemberByStatusCommand implements Command {

    private final AppContext context;

    public SearchMemberByStatusCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Member By Status ===");
        String status = context.input.readString("Enter status (ACTIVE, SUSPENDED, EXPIRED): ");
        MemberStatus memberStatus;
        try {
            memberStatus = MemberStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status — use ACTIVE, SUSPENDED, or EXPIRED");
            return;
        }
        List<Member> members = context.memberDAO.findByStatus(memberStatus);
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        members.forEach(member -> System.out.println(context.formatter.formatMember(member)));
    }
}
