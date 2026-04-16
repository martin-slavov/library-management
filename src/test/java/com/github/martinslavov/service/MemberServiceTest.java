package com.github.martinslavov.service;

import com.github.martinslavov.dao.FineDAO;
import com.github.martinslavov.dao.LoanDAO;
import com.github.martinslavov.dao.MemberDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberDAO memberDAO;

    @Mock
    FineDAO fineDAO;

    @Mock
    LoanDAO loanDAO;

    @InjectMocks
    MemberService memberService;

    private Member validMember;

    @BeforeEach
    void setUp() {
        validMember = new Member("Maria", "Ivanova", "+ 359 87 678 3376",
                "maria@example.com", LocalDate.now().plusYears(1));
    }

    @Test
    @DisplayName("Register member with valid data should save member")
    void registerMember_withValidData_shouldSaveMember() {
        when(memberDAO.findByEmail("maria@example.com")).thenReturn(Optional.empty());

        memberService.registerMember(validMember);

        verify(memberDAO).save(validMember);
    }

    @Test
    @DisplayName("Register member with invalid email should throw LibraryException")
    void registerMember_withInvalidEmail_shouldThrowLibraryException() {
        Member member = new Member("Maria", "Ivanova", null, "maria.example.com", LocalDate.now().plusYears(1));

        assertThrows(LibraryException.class, () -> memberService.registerMember(member));
    }

    @Test
    @DisplayName("Register member with invalid phone should throw LibraryException")
    void registerMember_withInvalidPhone_shouldThrowLibraryException() {
        Member member = new Member("Maria", "Ivanova", "abc", "maria@example.com", LocalDate.now().plusYears(1));

        assertThrows(LibraryException.class, () -> memberService.registerMember(member));
    }

    @Test
    @DisplayName("Register member with expire date before start date should throw LibraryException")
    void registerMember_withExpireDateBeforeStartDate_shouldThrowLibraryException() {
        Member member = new Member("Maria", "Ivanova", null, "maria@example.com", LocalDate.now().minusDays(1));

        assertThrows(LibraryException.class, () -> memberService.registerMember(member));
    }

    @Test
    @DisplayName("Register member with duplicate email should throw LibraryException")
    void registerMember_withDuplicateEmail_shouldThrowLibraryException() {
        when(memberDAO.findByEmail("maria@example.com")).thenReturn(Optional.of(validMember));

        assertThrows(LibraryException.class, () -> memberService.registerMember(validMember));
    }

    @Test
    @DisplayName("Suspend member with valid data should update member")
    void suspendMember_withValidData_shouldUpdateMember() {
        when(memberDAO.findById(validMember.getMemberId())).thenReturn(Optional.of(validMember));

        memberService.suspendMember(validMember);

        verify(memberDAO).update(validMember);
    }

    @Test
    @DisplayName("Suspend member with member not found throw LibraryException")
    void suspendMember_withMemberNotFound_shouldThrowLibraryException() {
        when(memberDAO.findById(validMember.getMemberId())).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> memberService.suspendMember(validMember));
    }

    @Test
    @DisplayName("Suspend member with already suspended member should throw LibraryException")
    void suspendMember_withAlreadySuspendedMember_shouldThrowLibraryException() {
        validMember.setStatus(MemberStatus.SUSPENDED);
        when(memberDAO.findById(validMember.getMemberId())).thenReturn(Optional.of(validMember));

        assertThrows(LibraryException.class, () -> memberService.suspendMember(validMember));
    }

    @Test
    @DisplayName("Suspend member with already expired member should throw LibraryException")
    void suspendMember_withAlreadyExpiredMember_shouldThrowLibraryException() {
        validMember.setStatus(MemberStatus.EXPIRED);
        when(memberDAO.findById(validMember.getMemberId())).thenReturn(Optional.of(validMember));

        assertThrows(LibraryException.class, () -> memberService.suspendMember(validMember));
    }

    @Test
    @DisplayName("Renew membership with valid data should update member")
    void renewMembership_withValidData_shouldUpdateMember() {
        when(memberDAO.findById(validMember.getMemberId())).thenReturn(Optional.of(validMember));
        when(loanDAO.findByMemberId(validMember.getMemberId())).thenReturn(Collections.emptyList());

        memberService.renewMembership(validMember, LocalDate.now().plusYears(1));

        verify(memberDAO).update(validMember);
    }

    @Test
    @DisplayName("Renew membership with suspended member should throw LibraryException")
    void renewMembership_withSuspendedMember_shouldThrowLibraryException() {
        validMember.setStatus(MemberStatus.SUSPENDED);
        when(memberDAO.findById(validMember.getMemberId())).thenReturn(Optional.of(validMember));

        assertThrows(LibraryException.class, () -> memberService.renewMembership(validMember, LocalDate.now().plusYears(1)));
    }

    @Test
    @DisplayName("Renew membership with expire date in past should throw LibraryException")
    void renewMembership_withExpireDateInPast_shouldThrowLibraryException() {
        when(memberDAO.findById(validMember.getMemberId())).thenReturn(Optional.of(validMember));
        when(loanDAO.findByMemberId(validMember.getMemberId())).thenReturn(Collections.emptyList());

        assertThrows(LibraryException.class, () -> memberService.renewMembership(validMember, LocalDate.now().minusDays(1)));
    }
}