package com.github.martinslavov.commands.fine;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Fine;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.FineStatus;
import com.github.martinslavov.util.AppContext;

import java.util.List;
import java.util.Optional;

public class WaiveFineCommand implements Command {

    private final AppContext context;

    public WaiveFineCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Waive Fine ===");
        Member member = context.selector.selectMember();
        if (member == null) return;

        List<Fine> unpaidFines = context.loanDAO.findByMemberId(member.getMemberId())
                .stream()
                .map(loan -> context.fineDAO.findByLoanId(loan.getLoanId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(fine -> fine.getStatus() == FineStatus.UNPAID)
                .toList();
        if (unpaidFines.isEmpty()) {
            System.out.println("No unpaid fines for this member.");
            return;
        }

        for (int i = 0; i < unpaidFines.size(); i++) {
            System.out.printf("%d. %s%n", i + 1,
                    context.formatter.formatFine(unpaidFines.get(i)));
        }

        try {
            int pick = context.input.readInt("Enter number: ") - 1;
            context.fineService.waiveFine(unpaidFines.get(pick));
            System.out.println("Fine waived paid successfully!");
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid selection");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
