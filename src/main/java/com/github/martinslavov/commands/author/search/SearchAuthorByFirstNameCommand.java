package com.github.martinslavov.commands.author.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAuthorByFirstNameCommand implements Command {

    private final AppContext context;

    public SearchAuthorByFirstNameCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Author By First Name ===");
        String firstName = context.input.readString("Enter first name: ");
        List<Author> authors = context.authorDAO.findByFirstName(firstName);
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
            return;
        }
        authors.forEach(author -> System.out.println(context.formatter.formatAuthor(author)));
    }
}
