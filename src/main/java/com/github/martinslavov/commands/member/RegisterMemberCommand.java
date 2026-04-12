package com.github.martinslavov.commands.member;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

import java.time.LocalDate;

public class RegisterMemberCommand implements Command {

    private final AppContext context;

    public RegisterMemberCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Register Member ===");
        String firstName = context.input.readString("Enter first name: ");
        String lastName = context.input.readString("Enter last name: ");
        String phone = context.input.readString("Enter phone number (press Enter to skip): ");
        if (phone.isBlank()) {
            phone = null;
        }
        String email = context.input.readString("Enter email: ");
        LocalDate expireDate = context.input.readDate("Enter expire date");

        Member member = new Member(firstName, lastName, phone, email, expireDate);
        try {
            context.memberService.registerMember(member);
            System.out.println("Member added successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
