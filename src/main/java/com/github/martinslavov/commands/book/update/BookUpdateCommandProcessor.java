package com.github.martinslavov.commands.book.update;

import com.github.martinslavov.model.Book;
import com.github.martinslavov.util.AppContext;
import com.github.martinslavov.util.MenuConstants;

public class BookUpdateCommandProcessor {

    private static final int UPDATE_TITLE = 1;
    private static final int UPDATE_AUTHOR = 2;
    private static final int UPDATE_CATEGORY = 3;
    private static final int ADD_COPIES = 4;
    private static final int BACK = 0;

    private final AppContext context;

    public BookUpdateCommandProcessor(AppContext context) {
        this.context = context;
    }

    public void run() {
        String isbn = context.input.readString("Enter ISBN of book to update: ");
        var optionalBook = context.bookDAO.findByIsbn(isbn);
        if (optionalBook.isEmpty()) {
            System.out.println("Book not found.");
            return;
        }
        Book book = optionalBook.get();

        boolean running = true;
        while (running) {
            System.out.printf(MenuConstants.BOOKS_UPDATE_MENU + "%n", isbn);
            int choice = context.input.readInt("");
            switch (choice) {
                case UPDATE_TITLE -> new UpdateBookTitleCommand(context, book).execute();
                case UPDATE_AUTHOR -> new UpdateBookAuthorCommand(context, book).execute();
                case UPDATE_CATEGORY -> new UpdateBookCategoryCommand(context, book).execute();
                case ADD_COPIES -> new AddBookCopiesCommand(context, book).execute();
                case BACK -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}
