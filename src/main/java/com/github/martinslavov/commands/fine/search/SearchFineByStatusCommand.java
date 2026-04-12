package com.github.martinslavov.commands.fine.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Fine;
import com.github.martinslavov.model.enums.FineStatus;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchFineByStatusCommand implements Command {

    private final AppContext context;

    public SearchFineByStatusCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Fines By Status ===");
        String statusString = context.input.readString("Enter status (PAID, UNPAID, WAIVED):");
        FineStatus status;
        try {
            status = FineStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status — use PAID, UNPAID or WAIVED");
            return;
        }
        List<Fine> fines = context.fineDAO.findByStatus(status);
        if (fines.isEmpty()) {
            System.out.println("No fine found");
            return;
        }
        fines.forEach(fine -> System.out.println(context.formatter.formatFine(fine)));
    }
}
