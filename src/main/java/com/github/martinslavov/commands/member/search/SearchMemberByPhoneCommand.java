package com.github.martinslavov.commands.member.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.util.Optional;

public class SearchMemberByPhoneCommand implements Command {

    private final AppContext context;

    public SearchMemberByPhoneCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Member By Phone ===");
        String phone = context.input.readString("Enter phone: ");
        Optional<Member> members = context.memberDAO.findByPhone(phone);
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        System.out.println(context.formatter.formatMember(members.get()));
    }
}
