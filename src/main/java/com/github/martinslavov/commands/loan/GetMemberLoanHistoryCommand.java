package com.github.martinslavov.commands.loan;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class GetMemberLoanHistoryCommand implements Command {

    private final AppContext context;

    public GetMemberLoanHistoryCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Get Member Loan History ===");
        Member member = context.selector.selectMember();
        if (member == null) {
            System.out.println("Member not found");
            return;
        }
        List<Loan> loans = context.loanDAO.getMemberLoanHistory(member.getMemberId());
        if (loans.isEmpty()) {
            System.out.println("No loan history found for this member.");
            return;
        }
        loans.forEach(loan -> System.out.println(context.formatter.formatLoan(loan)));
    }
}
