package com.github.martinslavov.commands.loan.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchLoanByBookCommand implements Command {

    private final AppContext context;

    public SearchLoanByBookCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Loans By Book ===");
        Book book = context.selector.selectBook();
        if (book == null) return;

        List<Loan> loans = context.loanDAO.findByBookId(book.getBookId());
        if (loans.isEmpty()) {
            System.out.println("No loans found for this book.");
            return;
        }
        loans.forEach(loan -> System.out.println(context.formatter.formatLoan(loan)));
    }
}
