package com.github.martinslavov.commands.member;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.ValidationHelper;

public class UpdateMemberCommand implements Command {

    private final AppContext context;

    public UpdateMemberCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        Member member = context.selector.selectMember();
        if (member == null) {
            System.out.println("Member not found");
            return;
        }
        String newFirstName = context.input.readString("Enter new first name (press Enter to keep '" + member.getFirstName() + "'): ");
        if (!newFirstName.isBlank()) {
            member.setFirstName(newFirstName);
        }
        String newLastName = context.input.readString("Enter new last name (press Enter to keep '" + member.getLastName() + "'): ");
        if (!newLastName.isBlank()) {
            member.setLastName(newLastName);
        }
        String newPhone = context.input.readString("Enter new phone (press Enter to keep '" + member.getPhone() + "'): ");
        if (!newPhone.isBlank()) {
            String cleanPhone = newPhone.replaceAll("[\\s-]", "");
            if (!ValidationHelper.isValidPhone(cleanPhone)) {
                System.err.println("Invalid phone format — phone not updated");
            } else {
                member.setPhone(cleanPhone);
            }
        }
        String newEmail = context.input.readString("Enter new email (press Enter to keep '" + member.getEmail() + "'): ");
        if (!newEmail.isBlank()) {
            if (!ValidationHelper.isValidEmail(newEmail)) {
                System.err.println("Invalid email format — email not updated");
            } else {
                member.setEmail(newEmail);
            }
        }
        try {
            context.memberDAO.update(member);
            System.out.println("Member updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
