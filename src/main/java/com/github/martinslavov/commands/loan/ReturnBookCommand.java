package com.github.martinslavov.commands.loan;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.LoanStatus;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class ReturnBookCommand implements Command {

    private final AppContext context;

    public ReturnBookCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Return Book ===");
        Member member = context.selector.selectMember();
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        List<Loan> activeLoans = context.loanDAO.findByMemberId(member.getMemberId())
                .stream()
                .filter(loan -> loan.getStatus() == LoanStatus.ACTIVE
                        || loan.getStatus() == LoanStatus.OVERDUE)
                .toList();
        if (activeLoans.isEmpty()) {
            System.out.println("No active loans for this member.");
            return;
        }

        for (int i = 0; i < activeLoans.size(); i++) {
            var book = context.bookDAO.findById(activeLoans.get(i).getBookId());
            if (book.isEmpty()) {
                System.out.println("Book not found");
            } else {
                System.out.printf("%d. %s | Due: %s | Status: %s%n",
                        i + 1,
                        book.get().getTitle(),
                        activeLoans.get(i).getEndDate(),
                        activeLoans.get(i).getStatus().name());
            }

        }
        try {
            int pick = context.input.readInt("Enter number: ") - 1;
            boolean hasFine = context.loanService.returnBook(activeLoans.get(pick).getLoanId());
            if (hasFine) {
                System.out.println("Book returned. Member has an unpaid fine.");
            } else {
                System.out.println("Book returned successfully!");
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid selection");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}