package com.github.martinslavov.util;

import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.model.Member;

import java.util.List;

public class EntitySelector {

    private final AppContext context;

    public EntitySelector(AppContext context) {
        this.context = context;
    }

    public Author selectAuthor(boolean allowAdd) {
        String firstName = context.input.readString("Enter author's first name: ");
        String lastName = context.input.readString("Enter author's last name: ");

        List<Author> authors = context.authorDAO.findByFullName(firstName, lastName);
        if (authors.isEmpty()) {
            if (allowAdd) {
                if (context.input.readConfirmation("Author not found. Do you want to add them?")) {
                    String nationality = context.input.readString("Enter nationality: ");
                    Author newAuthor = new Author(firstName, lastName, nationality);
                    try {
                        context.authorService.addAuthor(newAuthor);
                        return newAuthor;
                    } catch (LibraryException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Author not found.");
            }
            return null;
        } else if (authors.size() == 1) {
            return authors.getFirst();
        } else {
            for (int i = 0; i < authors.size(); i++) {
                System.out.println((i + 1) + ". " +
                        authors.get(i).getFirstName() + " " +
                        authors.get(i).getLastName() + " (" +
                        authors.get(i).getNationality() + ")");
            }
            try {
                int pick = context.input.readInt("Enter number: ") - 1;
                return authors.get(pick);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Invalid selection");
                return null;
            }
        }
    }

    public Category selectCategory(boolean allowAdd) {
        String categoryName = context.input.readString("Enter category name: ");

        List<Category> categories = context.categoryDAO.findByName(categoryName);
        if (categories.isEmpty()) {
            if (allowAdd) {
                if (context.input.readConfirmation("Category not found. Do you want to add it?")) {
                    String description = context.input.readString("Enter description: ");
                    Category newCategory = new Category(categoryName, description);
                    try {
                        context.categoryService.addCategory(newCategory);
                        return newCategory;
                    } catch (LibraryException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Category not found.");
            }
            return null;
        } else if (categories.size() == 1) {
            return categories.getFirst();
        } else {
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " +
                        categories.get(i).getName() + " (" +
                        categories.get(i).getDescription() + ")");
            }
            try {
                int pick = context.input.readInt("Enter number: ") - 1;
                return categories.get(pick);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Invalid selection");
                return null;
            }
        }
    }

    public Book selectBook() {
        String isbn = context.input.readString("Enter ISBN: ");
        return context.bookDAO.findByIsbn(isbn).orElse(null);
    }

    public Member selectMember() {
        String email = context.input.readString("Enter member email: ");
        return context.memberDAO.findByEmail(email).orElse(null);
    }
}