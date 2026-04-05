package com.github.martinslavov.model;

import com.github.martinslavov.model.enums.FineStatus;

import java.math.BigDecimal;

public class Fine {

    private int fineId;
    private int loanId;
    private BigDecimal amount;
    private FineStatus status;

    public Fine(int fineId, int loanId, BigDecimal  amount, FineStatus status) {
        this.fineId = fineId;
        this.loanId = loanId;
        this.amount = amount;
        this.status = status;
    }

    public Fine(int loanId, BigDecimal  amount, FineStatus status) {
        this(-1, loanId, amount, status);
    }

    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public BigDecimal  getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal  amount) {
        this.amount = amount;
    }

    public FineStatus getStatus() {
        return status;
    }

    public void setStatus(FineStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Fine{" +
                "fineId=" + fineId +
                ", loanId=" + loanId +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}
