package com.github.martinslavov.commands.member.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.util.Optional;

public class SearchMemberByEmailCommand implements Command {

    private final AppContext context;

    public SearchMemberByEmailCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Member By Email ===");
        String email = context.input.readString("Enter email: ");
        Optional<Member> members = context.memberDAO.findByEmail(email);
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        System.out.println(context.formatter.formatMember(members.get()));
    }
}
