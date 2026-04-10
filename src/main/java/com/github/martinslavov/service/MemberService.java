package com.github.martinslavov.service;

import com.github.martinslavov.dao.FineDAO;
import com.github.martinslavov.dao.LoanDAO;
import com.github.martinslavov.dao.MemberDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Loan;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.model.enums.FineStatus;
import com.github.martinslavov.model.enums.MemberStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class MemberService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s-]{7,14}$");

    private final MemberDAO memberDAO;
    private final FineDAO fineDAO;
    private final LoanDAO loanDAO;

    public MemberService(MemberDAO memberDAO, FineDAO fineDAO, LoanDAO loanDAO) {
        this.memberDAO = memberDAO;
        this.fineDAO = fineDAO;
        this.loanDAO = loanDAO;
    }

    public void registerMember(Member member) {

        if (!EMAIL_PATTERN.matcher(member.getEmail()).matches()) {
            throw new LibraryException("Invalid email format");
        }

        if (member.getPhone() != null) {
            String cleanPhone = member.getPhone().replaceAll("[\\s-]", "");
            member.setPhone(cleanPhone);
            if (!PHONE_PATTERN.matcher(member.getPhone().replaceAll("[\\s-]", "")).matches()) {
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
