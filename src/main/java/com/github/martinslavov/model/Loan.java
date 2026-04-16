package com.github.martinslavov.model;

import com.github.martinslavov.model.enums.LoanStatus;

import java.time.LocalDate;

public class Loan {

    private int loanId;
    private final int bookId;
    private final int memberId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate returnDate;
    private LoanStatus status;

    public Loan(int loanId, int bookId, int memberId, LocalDate startDate, LocalDate endDate, LocalDate returnDate, LoanStatus status) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Loan(int bookId, int memberId, LocalDate startDate, LocalDate endDate, LocalDate returnDate, LoanStatus status) {
        this(-1, bookId, memberId, startDate, endDate, returnDate, status);
    }

    public Loan(int bookId, int memberId, LocalDate startDate, LocalDate endDate, LoanStatus status) {
        this(-1, bookId, memberId, startDate, endDate, null, status);
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}
