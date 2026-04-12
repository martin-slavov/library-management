package com.github.martinslavov.commands.author;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.util.AppContext;

public class AddAuthorCommand implements Command {

    private final AppContext context;

    public AddAuthorCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Add Author ===");
        String firstName = context.input.readString("Enter first name: ");
        String lastName = context.input.readString("Enter last name: ");
        String nationality = context.input.readString("Enter nationality: ");

        Author author = new Author(firstName, lastName, nationality);
        try {
            context.authorService.addAuthor(author);
            System.out.println("Author added successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
