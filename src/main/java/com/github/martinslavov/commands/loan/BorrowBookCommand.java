package com.github.martinslavov.commands.loan;

import com.github.martinslavov.commands.Command;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Member;
import com.github.martinslavov.util.AppContext;

public class BorrowBookCommand implements Command {

    private final AppContext context;

    public BorrowBookCommand(AppContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        System.out.println("=== Borrow Book ===");
        Member member = context.selector.selectMember();
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        Book book = context.selector.selectBook();
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        try {
            context.loanService.borrowBook(member.getMemberId(), book.getBookId());
            System.out.println("Book borrowed successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
