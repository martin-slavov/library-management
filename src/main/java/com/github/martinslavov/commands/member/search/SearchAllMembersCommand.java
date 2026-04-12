package com.github.martinslavov.commands.member.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAllMembersCommand implements Command {

    private final AppContext context;

    public SearchAllMembersCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== All Members ===");
        List<Member> members = context.memberDAO.findAll();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        members.forEach(member -> System.out.println(context.formatter.formatMember(member)));
    }
}
