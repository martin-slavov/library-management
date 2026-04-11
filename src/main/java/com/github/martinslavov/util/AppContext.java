package com.github.martinslavov.util;

import com.github.martinslavov.dao.*;
import com.github.martinslavov.scheduler.OverdueScanner;
import com.github.martinslavov.service.*;

import java.util.Scanner;

public class AppContext {

    public final Scanner scanner;

    public final BookDAO bookDAO;
    public final AuthorDAO authorDAO;
    public final CategoryDAO categoryDAO;
    public final FineDAO fineDAO;
    public final LoanDAO loanDAO;
    public final MemberDAO memberDAO;

    public final AuthorService authorService;
    public final BookService bookService;
    public final CategoryService categoryService;
    public final FineService fineService;
    public final LoanService loanService;
    public final MemberService memberService;

    public final OverdueScanner overdueScanner;

    public final InputHelper input;
    public final EntitySelector selector;
    public final FormatterHelper formatter;

    public AppContext() {
        this.scanner = new Scanner(System.in);
        this.bookDAO = new BookDAO();
        this.authorDAO = new AuthorDAO();
        this.categoryDAO = new CategoryDAO();
        this.fineDAO = new FineDAO();
        this.loanDAO = new LoanDAO();
        this.memberDAO = new MemberDAO();
        this.authorService = new AuthorService(authorDAO, bookDAO);
        this.bookService = new BookService(bookDAO, authorDAO, categoryDAO);
        this.categoryService = new CategoryService(categoryDAO, bookDAO);
        this.fineService = new FineService(fineDAO);
        this.loanService = new LoanService(loanDAO, bookDAO, memberDAO, fineDAO);
        this.memberService = new MemberService(memberDAO, fineDAO, loanDAO);
        this.overdueScanner = new OverdueScanner(loanDAO);
        this.input = new InputHelper(scanner);
        this.selector = new EntitySelector(this);
        this.formatter = new FormatterHelper(this);
    }
}
