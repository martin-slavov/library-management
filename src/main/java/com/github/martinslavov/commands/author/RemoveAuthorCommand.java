package com.github.martinslavov.commands.author;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.util.AppContext;

public class RemoveAuthorCommand implements Command {

    private final AppContext context;

    public RemoveAuthorCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Remove Author ===");
        Author author = context.selector.selectAuthor(false);
        if (author == null) return;
        if (context.input.readConfirmation(String.format("Are you sure you want to delete %s %s", author.getFirstName(), author.getLastName()))) {
            try {
                context.authorService.removeAuthor(author);
                System.out.println("Author deleted successfully!");
            } catch (LibraryException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Author not deleted!");
        }
    }
}
