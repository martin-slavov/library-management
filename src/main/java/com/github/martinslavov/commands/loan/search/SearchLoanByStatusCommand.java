package com.github.martinslavov.commands.loan.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.enums.LoanStatus;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchLoanByStatusCommand implements Command {

    private final AppContext context;

    public SearchLoanByStatusCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Loans By Status ===");
        String status = context.input.readString("Enter status (ACTIVE, RETURNED, OVERDUE): ");
        LoanStatus loanStatus;
        try {
            loanStatus = LoanStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status — use ACTIVE, RETURNED, or OVERDUE");
            return;
        }

        List<Loan> loans = context.loanDAO.findByStatus(loanStatus);
        if (loans.isEmpty()) {
            System.out.println("No loans found for this status.");
            return;
        }
        loans.forEach(loan -> System.out.println(context.formatter.formatLoan(loan)));
    }
}
