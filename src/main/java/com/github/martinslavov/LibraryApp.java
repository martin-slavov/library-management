package com.github.martinslavov;

import com.github.martinslavov.dao.*;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import com.github.martinslavov.scheduler.OverdueScanner;
import com.github.martinslavov.service.*;
import com.github.martinslavov.util.MenuConstants;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class LibraryApp {

    private final Scanner scanner = new Scanner(System.in);

    private final OverdueScanner overdueScanner;

    private final AuthorDAO authorDAO;
    private final BookDAO bookDAO;
    private final CategoryDAO categoryDAO;
    private final FineDAO fineDAO;
    private final LoanDAO loanDAO;
    private final MemberDAO memberDAO;

    private final AuthorService authorService;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final FineService fineService;
    private final LoanService loanService;
    private final MemberService memberService;

    public LibraryApp() {
        this.authorDAO = new AuthorDAO();
        this.bookDAO = new BookDAO();
        this.categoryDAO = new CategoryDAO();
        this.fineDAO = new FineDAO();
        this.loanDAO = new LoanDAO();
        this.memberDAO = new MemberDAO();
        this.overdueScanner = new OverdueScanner(loanDAO);
        this.authorService = new AuthorService(authorDAO, bookDAO);
        this.bookService = new BookService(bookDAO, authorDAO, categoryDAO);
        this.categoryService = new CategoryService(categoryDAO, bookDAO);
        this.fineService = new FineService(fineDAO);
        this.loanService = new LoanService(loanDAO, bookDAO, memberDAO, fineDAO);
        this.memberService = new MemberService(memberDAO, fineDAO, loanDAO);
    }

    public void run() {
        overdueScanner.start();

        boolean running = true;
        while (running) {
            System.out.print(MenuConstants.MAIN_MENU);
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input — please enter a number");
                continue;
            }
            switch (choice) {
                case 1 -> handleBooks();
                case 2 -> handleMembers();
                case 3 -> handleAuthors();
                case 4 -> handleCategories();
                case 5 -> handleLoans();
                case 6 -> handleFines();
                case 0 -> {
                    overdueScanner.stop();
                    running = false;
                }
                default -> System.out.println("Invalid option — try again");
            }
        }
    }
}