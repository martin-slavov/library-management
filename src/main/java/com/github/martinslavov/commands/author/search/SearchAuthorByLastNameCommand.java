package com.github.martinslavov.commands.author.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAuthorByLastNameCommand implements Command {

    private final AppContext context;

    public SearchAuthorByLastNameCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Author By Last Name ===");
        String lastName = context.input.readString("Enter last name: ");
        List<Author> authors = context.authorDAO.findByLastName(lastName);
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
            return;
        }
        authors.forEach(author -> System.out.println(context.formatter.formatAuthor(author)));
    }
}
