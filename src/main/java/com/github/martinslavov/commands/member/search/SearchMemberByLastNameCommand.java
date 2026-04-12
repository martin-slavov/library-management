package com.github.martinslavov.commands.member.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchMemberByLastNameCommand implements Command {

    private final AppContext context;

    public SearchMemberByLastNameCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Member By Last Name ===");
        String lastName = context.input.readString("Enter last name: ");
        List<Member> members = context.memberDAO.findByLastName(lastName);
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        members.forEach(member -> System.out.println(context.formatter.formatMember(member)));
    }
}
