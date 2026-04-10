package com.github.martinslavov.service;

import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.dao.FineDAO;
import com.github.martinslavov.dao.LoanDAO;
import com.github.martinslavov.dao.MemberDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.enums.FineStatus;
import com.github.martinslavov.model.enums.LoanStatus;

import java.util.List;

public class LoanService {

    public static final int BORROW_NO_COPIES = 1;
    public static final int BORROW_MEMBER_INACTIVE = 2;
    public static final int RETURN_FAILED = 1;
    public static final int RETURN_WITH_FINE = 2;

    private final LoanDAO loanDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final FineDAO fineDAO;

    public LoanService(LoanDAO loanDAO, BookDAO bookDAO, MemberDAO memberDAO, FineDAO fineDAO) {
        this.loanDAO = loanDAO;
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
        this.fineDAO = fineDAO;
    }

    public void borrowBook(int memberId, int bookId) {

        if (memberDAO.findById(memberId).isEmpty()) {
            throw new LibraryException("Member not found");
        }

        if (bookDAO.findById(bookId).isEmpty()) {
            throw new LibraryException("Book not found");
        }

        List<Loan> memberLoans = loanDAO.findByMemberId(memberId);
        boolean hasFine = memberLoans.stream()
                .anyMatch(loan -> fineDAO.findByLoanId(loan.getLoanId())
                        .map(fine -> fine.getStatus() == FineStatus.UNPAID)
                        .orElse(false));
        if (hasFine) {
            throw new LibraryException("Member has unpaid fines");
        }

        boolean activeLoan = memberLoans.stream()
                .anyMatch(loan -> loan.getBookId() == bookId && loan.getStatus() == LoanStatus.ACTIVE);
        if (activeLoan) {
            throw new LibraryException("Member already has this book on loan");
        }

        int result = loanDAO.borrowBook(memberId, bookId);
        switch (result) {
            case BORROW_NO_COPIES:
                throw new LibraryException("There is not available copy");
            case BORROW_MEMBER_INACTIVE:
                throw new LibraryException("Member is inactive");
        }
    }

    public boolean returnBook(int loanId) {

        if (loanDAO.findById(loanId).isEmpty()) {
            throw new LibraryException("Loan not found");
        }

        int result = loanDAO.returnBook(loanId);
        if (result == RETURN_FAILED) {
            throw new LibraryException("Loan not found or already returned");
        }

        return result == RETURN_WITH_FINE;
    }
}
