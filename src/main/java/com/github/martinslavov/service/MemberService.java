package com.github.martinslavov.service;

import com.github.martinslavov.dao.FineDAO;
import com.github.martinslavov.dao.LoanDAO;
import com.github.martinslavov.dao.MemberDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.FineStatus;
import com.github.martinslavov.model.enums.MemberStatus;
import com.github.martinslavov.util.ValidationHelper;

import java.time.LocalDate;
import java.util.List;

public class MemberService {

    private final MemberDAO memberDAO;
    private final FineDAO fineDAO;
    private final LoanDAO loanDAO;

    public MemberService(MemberDAO memberDAO, FineDAO fineDAO, LoanDAO loanDAO) {
        this.memberDAO = memberDAO;
        this.fineDAO = fineDAO;
        this.loanDAO = loanDAO;
    }

    public void registerMember(Member member) {

        if (!ValidationHelper.isValidEmail(member.getEmail())) {
            throw new LibraryException("Invalid email format");
        }

        if (member.getPhone() != null) {
            String cleanPhone = member.getPhone().replaceAll("[\\s-]", "");
            member.setPhone(cleanPhone);
            if (!ValidationHelper.isValidPhone(cleanPhone)) {
                throw new LibraryException("Invalid phone format");
            }
        }

        if (member.getExpireDate().isBefore(member.getStartDate())) {
            throw new LibraryException("Expire date must be after start date");
        }

        if (memberDAO.findByEmail(member.getEmail()).isPresent()) {
            throw new LibraryException("Email already exists");
        }

        memberDAO.save(member);
    }

    public void suspendMember(Member member) {

        validateMemberExists(member);
        validateMemberNotSuspended(member);

        if (member.getStatus().equals(MemberStatus.EXPIRED)) {
            throw new LibraryException("Member is expired");
        }

        member.setStatus(MemberStatus.SUSPENDED);
        memberDAO.update(member);
    }

    public void renewMembership(Member member, LocalDate newExpireDate) {

        validateMemberExists(member);
        validateMemberNotSuspended(member);

        List<Loan> membersLoan = loanDAO.findByMemberId(member.getMemberId());
        boolean hasFine = membersLoan.stream()
                .anyMatch(loan -> fineDAO.findByLoanId(loan.getLoanId())
                        .map(fine -> fine.getStatus() == FineStatus.UNPAID)
                        .orElse(false));
        if (hasFine) {
            throw new LibraryException("Member has unpaid fine");
        }

        if (newExpireDate.isBefore(LocalDate.now())) {
            throw new LibraryException("New expire date must be in the future");
        }

        member.setExpireDate(newExpireDate);
        member.setStatus(MemberStatus.ACTIVE);

        memberDAO.update(member);
    }

    public boolean isMembershipValid(Member member) {
        return member.getStatus() == MemberStatus.ACTIVE
                && !member.getExpireDate().isBefore(LocalDate.now());
    }

    private void validateMemberExists(Member member) {
        if (memberDAO.findById(member.getMemberId()).isEmpty()) {
            throw new LibraryException("Member not found");
        }
    }

    private void validateMemberNotSuspended(Member member) {
        if (member.getStatus().equals(MemberStatus.SUSPENDED)) {
            throw new LibraryException("Member is already suspended");
        }
    }
}
