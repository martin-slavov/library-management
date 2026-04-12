package com.github.martinslavov.commands.author;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.util.AppContext;

public class UpdateAuthorCommand implements Command {

    private final AppContext context;

    public UpdateAuthorCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        Author author = context.selector.selectAuthor(false);
        if (author == null) {
            System.out.println("Author not found");
            return;
        }

        String newFirstName = context.input.readString("Enter new first name (press Enter to keep '" + author.getFirstName() + "'): ");
        if (!newFirstName.isBlank()) {
            author.setFirstName(newFirstName);
        }
        String newLastName = context.input.readString("Enter new last name (press Enter to keep '" + author.getLastName() + "'): ");
        if (!newLastName.isBlank()) {
            author.setLastName(newLastName);
        }
        String newNationality = context.input.readString("Enter new nationality (press Enter to keep '" + author.getNationality() + "'): ");
        if (!newNationality.isBlank()) {
            author.setNationality(newNationality);
        }

        try {
            context.authorDAO.update(author);
            System.out.println("Author updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
