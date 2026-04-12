package com.github.martinslavov.commands.author.search;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.util.AppContext;

import java.util.List;

public class SearchAuthorByNationalityCommand implements Command {

    private final AppContext context;

    public SearchAuthorByNationalityCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Search Author By Nationality ===");
        String nationality = context.input.readString("Enter nationality: ");
        List<Author> authors = context.authorDAO.findByNationality(nationality);
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
            return;
        }
        authors.forEach(author -> System.out.println(context.formatter.formatAuthor(author)));
    }
}
