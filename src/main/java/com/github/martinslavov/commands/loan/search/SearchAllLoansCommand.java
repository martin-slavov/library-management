package com.github.martinslavov.commands.loan.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAllLoansCommand implements Command {

    private final AppContext context;

    public SearchAllLoansCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== All Loans ===");
        List<Loan> loans = context.loanDAO.findAll();
        if (loans.isEmpty()) {
            System.out.println("No loans found");
            return;
        }
        loans.forEach(loan -> System.out.println(context.formatter.formatLoan(loan)));
    }
}
