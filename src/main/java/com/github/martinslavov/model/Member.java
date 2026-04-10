package com.github.martinslavov.model;

import com.github.martinslavov.model.enums.MemberStatus;

import java.time.LocalDate;

public class Member {

    private int memberId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private LocalDate startDate;
    private LocalDate expireDate;
    private MemberStatus status;

    public Member(int memberId, String firstName, String lastName, String phone, String email, LocalDate startDate, LocalDate expireDate, MemberStatus status) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.startDate = startDate;
        this.expireDate = expireDate;
        this.status = status;
    }

    public Member(String firstName, String lastName, String phone, String email, LocalDate startDate, LocalDate expireDate, MemberStatus status) {
        this(-1, firstName, lastName, phone, email, startDate, expireDate, status);
    }

    public Member(String firstName, String lastName, String phone, String email, LocalDate expireDate) {
        this(-1, firstName, lastName, phone, email, LocalDate.now(), expireDate, MemberStatus.ACTIVE);
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", startDate=" + startDate +
                ", expireDate=" + expireDate +
                ", status=" + status +
                '}';
    }
}