package com.github.martinslavov.service;

import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.dao.FineDAO;
import com.github.martinslavov.dao.LoanDAO;
import com.github.martinslavov.dao.MemberDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Fine;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.FineStatus;
import com.github.martinslavov.model.enums.LoanStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    LoanDAO loanDAO;

    @Mock
    BookDAO bookDAO;

    @Mock
    MemberDAO memberDAO;

    @Mock
    FineDAO fineDAO;

    @InjectMocks
    LoanService loanService;

    private Member validMember;
    private Book validBook;
    private int memberId;
    private int bookId;

    @BeforeEach
    void setUp() {
        validMember = new Member("Maria", "Ivanova", null, "maria@example.com", LocalDate.now().plusYears(1));
        validBook = new Book("9780441172719", "Dune", 1, 1, 1965, 3, 3);
        memberId = validMember.getMemberId();
        bookId = validBook.getBookId();
    }

    @Test
    @DisplayName("Borrow book with valid data should borrow book")
    void borrowBook_withValidData_shouldBorrowBook() {
        when(memberDAO.findById(memberId)).thenReturn(Optional.of(validMember));
        when(bookDAO.findById(bookId)).thenReturn(Optional.of(validBook));
        when(loanDAO.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(loanDAO.borrowBook(memberId, bookId)).thenReturn(0);

        loanService.borrowBook(memberId, bookId);

        verify(loanDAO).borrowBook(memberId, bookId);
    }

    @Test
    @DisplayName("Borrow book with member not found should throw LibraryException")
    void borrowBook_withMemberNotFound_shouldThrowLibraryException() {
        when(memberDAO.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> loanService.borrowBook(memberId, bookId));
    }

    @Test
    @DisplayName("Borrow book with book not found should throw LibraryException")
    void borrowBook_withBookNotFound_shouldThrowLibraryException() {
        when(memberDAO.findById(memberId)).thenReturn(Optional.of(validMember));
        when(bookDAO.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> loanService.borrowBook(memberId, bookId));
    }

    @Test
    @DisplayName("Borrow book with unpaid fines should throw LibraryException")
    void borrowBook_withUnpaidFines_shouldThrowLibraryException() {
        Loan loanWithFine = new Loan(1, bookId, memberId,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(16),
                LocalDate.now().minusDays(1), LoanStatus.RETURNED);

        Fine unpaidFine = new Fine(1, loanWithFine.getLoanId(), new BigDecimal("5.00"), FineStatus.UNPAID);

        when(memberDAO.findById(memberId)).thenReturn(Optional.of(validMember));
        when(bookDAO.findById(bookId)).thenReturn(Optional.of(validBook));
        when(loanDAO.findByMemberId(memberId)).thenReturn(List.of(loanWithFine));
        when(fineDAO.findByLoanId(loanWithFine.getLoanId())).thenReturn(Optional.of(unpaidFine));

        assertThrows(LibraryException.class, () -> loanService.borrowBook(memberId, bookId));
    }

    @Test
    @DisplayName("Borrow book with no copies available should throw LibraryException")
    void borrowBook_withNoCopiesAvailable_shouldThrowLibraryException() {
        when(memberDAO.findById(memberId)).thenReturn(Optional.of(validMember));
        when(bookDAO.findById(bookId)).thenReturn(Optional.of(validBook));
        when(loanDAO.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(loanDAO.borrowBook(memberId, bookId)).thenReturn(1);

        assertThrows(LibraryException.class, () -> loanService.borrowBook(memberId, bookId));
    }

    @Test
    @DisplayName("Borrow book with member inactive should throw LibraryException")
    void borrowBook_withMemberInactive_shouldThrowLibraryException() {
        when(memberDAO.findById(memberId)).thenReturn(Optional.of(validMember));
        when(bookDAO.findById(bookId)).thenReturn(Optional.of(validBook));
        when(loanDAO.findByMemberId(memberId)).thenReturn(Collections.emptyList());
        when(loanDAO.borrowBook(memberId, bookId)).thenReturn(2);

        assertThrows(LibraryException.class, () -> loanService.borrowBook(memberId, bookId));
    }

    @Test
    @DisplayName("Borrow book with already has book on loan should throw LibraryException")
    void borrowBook_withAlreadyHasBookOnLoan_shouldThrowLibraryException() {
        Loan activeLoan = new Loan(1, bookId, memberId,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(9),
                null, LoanStatus.ACTIVE);

        when(memberDAO.findById(memberId)).thenReturn(Optional.of(validMember));
        when(bookDAO.findById(bookId)).thenReturn(Optional.of(validBook));
        when(loanDAO.findByMemberId(memberId)).thenReturn(List.of(activeLoan));

        assertThrows(LibraryException.class, () -> loanService.borrowBook(memberId, bookId));
    }

    @Test
    @DisplayName("Return book with valid data should return false")
    void returnBook_withValidData_shouldReturnFalse() {
        Loan loan = new Loan(1, bookId, memberId,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(9),
                null, LoanStatus.ACTIVE);

        int loanId = loan.getLoanId();

        when(loanDAO.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanDAO.returnBook(loanId)).thenReturn(0);

        assertFalse(loanService.returnBook(loanId));
        verify(loanDAO).returnBook(loanId);
    }

    @Test
    @DisplayName("Return book with loan not found should throw LibraryException")
    void returnBook_withLoanNotFound_shouldThrowLibraryException() {
        int loanId = 1;
        when(loanDAO.findById(loanId)).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> loanService.returnBook(loanId));
    }

    @Test
    @DisplayName("Return book with failed return should throw LibraryException")
    void returnBook_withFailedReturn_shouldThrowLibraryException() {
        Loan loan = new Loan(1, bookId, memberId,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(16),
                LocalDate.now().minusDays(1), LoanStatus.RETURNED);

        int loanId = loan.getLoanId();

        when(loanDAO.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanDAO.returnBook(loanId)).thenReturn(1);

        assertThrows(LibraryException.class, () -> loanService.returnBook(loanId));
    }

    @Test
    @DisplayName("Return book with fine generated should return true")
    void returnBook_withFineGenerated_shouldReturnTrue() {
        Loan loan = new Loan(1, bookId, memberId,
                LocalDate.now().minusDays(30), LocalDate.now().minusDays(16),
                null, LoanStatus.ACTIVE);

        int loanId = loan.getLoanId();

        when(loanDAO.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanDAO.returnBook(loanId)).thenReturn(2);

        assertTrue(loanService.returnBook(loanId));
    }
}