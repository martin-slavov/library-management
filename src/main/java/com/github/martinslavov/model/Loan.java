package com.github.martinslavov.model;

import com.github.martinslavov.model.enums.LoanStatus;

import java.time.LocalDate;

public class Loan {

    private int loanId;
    private int bookId;
    private int memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate returnDate;
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

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
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
