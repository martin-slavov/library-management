package com.github.martinslavov.commands.fine.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Fine;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAllFinesCommand implements Command {

    private final AppContext context;

    public SearchAllFinesCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== All Fines ===");
        List<Fine> fines = context.fineDAO.findAll();
        if (fines.isEmpty()) {
            System.out.println("No fines found");
            return;
        }
        fines.forEach(fine -> System.out.println(context.formatter.formatFine(fine)));
    }
}
