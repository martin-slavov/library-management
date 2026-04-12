package com.github.martinslavov.commands.loan.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchLoanByMemberCommand implements Command {

    private final AppContext context;

    public SearchLoanByMemberCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Loans By Member ===");
        Member member = context.selector.selectMember();
        if (member == null) return;

        List<Loan> loans = context.loanDAO.findByMemberId(member.getMemberId());
        if (loans.isEmpty()) {
            System.out.println("No loans found for this member.");
            return;
        }
        loans.forEach(loan -> System.out.println(context.formatter.formatLoan(loan)));
    }
}
