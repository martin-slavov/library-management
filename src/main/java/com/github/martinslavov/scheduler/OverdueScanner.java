package com.github.martinslavov.scheduler;

import com.github.martinslavov.dao.LoanDAO;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.enums.LoanStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OverdueScanner {

    private final LoanDAO loanDAO;
    private final ScheduledExecutorService scheduler;

    public OverdueScanner(LoanDAO loanDAO) {
        this.loanDAO = loanDAO;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void scan() {
        List<Loan> allActiveLoans = loanDAO.findByStatus(LoanStatus.ACTIVE);
        allActiveLoans.stream()
                .filter(loan -> loan.getEndDate().isBefore(LocalDate.now()))
                .forEach(loan -> {
                    loanDAO.updateStatus(loan, LoanStatus.OVERDUE);
                    loanDAO.calculateFine(loan.getLoanId());
                });
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::scan, 0, 24, TimeUnit.HOURS);
    }

    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
