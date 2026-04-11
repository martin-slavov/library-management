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

    private void handleBooks() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.BOOKS_MENU);
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input — please enter a number");
                continue;
            }
            switch (choice) {
                case 1 -> addBook();
                case 2 -> removeBook();
                case 3 -> searchBook();
                case 4 -> updateBook();
                case 0 -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }

    private void addBook() {
        System.out.println("=== Add Book ===");

        System.out.println("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();

        System.out.println("Enter title: ");
        String title = scanner.nextLine().trim();

        Author selectedAuthor = selectAuthor(true);
        if (selectedAuthor == null) return;

        Category selectedCategory = selectCategory(true);
        if (selectedCategory == null) return;

        System.out.println("Enter publication year: ");
        int publicationYear;
        try {
            publicationYear = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input — please enter a number");
            return;
        }

        System.out.println("Enter total copies: ");
        int totalCopies;
        try {
            totalCopies = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input — please enter a number");
            return;
        }

        System.out.println("Enter available copies: ");
        int availableCopies;
        try {
            availableCopies = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input — please enter a number");
            return;
        }

        Book book = new Book(isbn, title, selectedAuthor.getAuthorId(), selectedCategory.getCategoryId(), publicationYear, totalCopies, availableCopies);
        try {
            bookService.addBook(book);
            System.out.println("Book added successfully!");
        } catch (LibraryException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void removeBook() {
        System.out.println("=== Remove book ===");

        System.out.println("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();

        var book = bookDAO.findByIsbn(isbn);
        if (book.isEmpty()) {
            System.out.println("Book not found");
            return;
        }
        System.out.printf("Are you sure you want to delete book with ISBN /%s/ (yes/no)%n", isbn);
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("yes")) {
            try {
                bookService.removeBook(book.get());
                System.out.println("Book deleted successfully!");
            } catch (LibraryException e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Book not deleted!");
        }
    }

    private void searchBook() {
        boolean running = true;
        while (running) {
            System.out.println(MenuConstants.BOOKS_SEARCH_MENU);
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input — please enter a number");
                continue;
            }
            switch (choice) {
                case 1 -> allBook();
                case 2 -> booksByIsbn();
                case 3 -> booksByTitle();
                case 4 -> booksByAuthor();
                case 5 -> booksByCategory();
                case 0 -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }

    private String formatBook(Book book) {
        String authorName = authorDAO.findById(book.getAuthorId())
                .map(a -> a.getFirstName() + " " + a.getLastName())
                .orElse("Unknown");

        String categoryName = categoryDAO.findById(book.getCategoryId())
                .map(Category::getName)
                .orElse("Unknown");

        return String.format("ID: %d | %s | %s | %s | %d | Copies: %d/%d",
                book.getBookId(), book.getTitle(), authorName, categoryName,
                book.getPublicationDate(), book.getAvailableCopies(), book.getTotalCopies());
    }

    private void allBook() {
        List<Book> books = bookDAO.findAll();
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        books.forEach(book -> System.out.println(formatBook(book)));
    }

    private void booksByIsbn() {
        System.out.println("=== Search Book By ISBN ===");
        System.out.println("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();

        Optional<Book> book = bookDAO.findByIsbn(isbn);
        if (book.isEmpty()) {
            System.out.println("Book not found found.");
            return;
        }
        System.out.println(formatBook(book.get()));
    }

    private void booksByTitle() {
        System.out.println("=== Search Book By Title ===");
        System.out.println("Enter title: ");
        String title = scanner.nextLine().trim();

        List<Book> books = bookDAO.findByTitle(title);
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        books.forEach(book -> System.out.println(formatBook(book)));
    }

    private void booksByAuthor() {
        System.out.println("=== Search Book By Author ===");
        Author selectedAuthor = selectAuthor(false);
        if (selectedAuthor == null) return;

        List<Book> books = bookDAO.findByAuthorId(selectedAuthor.getAuthorId());
        if (books.isEmpty()) {
            System.out.println("No books found for this author.");
            return;
        }
        books.forEach(book -> System.out.println(formatBook(book)));
    }

    private void booksByCategory() {
        System.out.println("=== Search Book By Category ===");
        Category selectedCategory = selectCategory(false);
        if (selectedCategory == null) return;

        List<Book> books = bookDAO.findByCategoryId(selectedCategory.getCategoryId());
        if (books.isEmpty()) {
            System.out.println("No books found for this category.");
            return;
        }
        books.forEach(book -> System.out.println(formatBook(book)));
    }

    private void updateBook() {
        System.out.println("=== Update Book ===");
        System.out.println("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();

        Optional<Book> book = bookDAO.findByIsbn(isbn);
        if (book.isEmpty()) {
            System.out.println("Book not found");
            return;
        }
        boolean running = true;
        while (running) {
            System.out.printf(MenuConstants.BOOKS_UPDATE_MENU + "%n", isbn);
            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input — please enter a number");
                continue;
            }
            Book bookObject = book.get();
            switch (choice) {
                case 1 -> updateTitle(bookObject);
                case 2 -> updateAuthor(bookObject);
                case 3 -> updateCategory(bookObject);
                case 4 -> addCopies(bookObject);
                case 0 -> running = false;
                default -> System.out.println("Invalid option — try again");
            }
        }
    }

    private void updateTitle(Book book) {
        System.out.print("Enter new title: ");
        String title = scanner.nextLine().trim();
        book.setTitle(title);
        try {
            bookDAO.update(book);
            System.out.println("Title updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void updateAuthor(Book book) {
        Author selectedAuthor = selectAuthor(false);
        if (selectedAuthor == null) {
            System.out.println("Author not found.");
            return;
        }

        book.setAuthorId(selectedAuthor.getAuthorId());
        try {
            bookDAO.update(book);
            System.out.println("Author updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void updateCategory(Book book) {
        Category selectedCategory = selectCategory(false);
        if (selectedCategory == null) {
            System.out.println("Category not found.");
            return;
        }

        book.setCategoryId(selectedCategory.getCategoryId());
        try {
            bookDAO.update(book);
            System.out.println("Category updated successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void addCopies(Book book) {
        System.out.print("Enter number of copies to add: ");
        int copies;
        try {
            copies = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input — please enter a number");
            return;
        }
        if (copies <= 0) {
            System.err.println("Number of copies must be greater than 0");
            return;
        }
        book.setTotalCopies(book.getTotalCopies() + copies);
        book.setAvailableCopies(book.getAvailableCopies() + copies);
        try {
            bookDAO.update(book);
            System.out.println("Copies added successfully!");
        } catch (DatabaseException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void handleMembers() {

    }

    private void handleAuthors() {

    }

    private void handleCategories() {

    }

    private void handleLoans() {

    }

    private void handleFines() {

    }

    private Author selectAuthor(boolean allowAdd) {
        System.out.print("Enter author's first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter author's last name: ");
        String lastName = scanner.nextLine().trim();

        List<Author> authors = authorDAO.findByFullName(firstName, lastName);
        if (authors.isEmpty()) {
            if (allowAdd) {
                System.out.println("Author not found. Do you want to add them? (yes/no)");
                String answer = scanner.nextLine().trim();
                if (answer.equalsIgnoreCase("yes")) {
                    System.out.print("Enter nationality: ");
                    String nationality = scanner.nextLine().trim();
                    Author newAuthor = new Author(firstName, lastName, nationality);
                    try {
                        authorService.addAuthor(newAuthor);
                        return newAuthor;
                    } catch (LibraryException e) {
                        System.err.println("Error: " + e.getMessage());
                        return null;
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
            System.out.print("Enter number: ");
            try {
                int pick = Integer.parseInt(scanner.nextLine()) - 1;
                return authors.get(pick);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input");
                return null;
            }
        }
    }

    private Category selectCategory(boolean allowAdd) {
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine().trim();

        List<Category> categories = categoryDAO.findByName(categoryName);
        if (categories.isEmpty()) {
            if (allowAdd) {
                System.out.println("Category not found. Do you want to add it? (yes/no)");
                String answer = scanner.nextLine().trim();
                if (answer.equalsIgnoreCase("yes")) {
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine().trim();
                    Category newCategory = new Category(categoryName, description);
                    try {
                        categoryService.addCategory(newCategory);
                        return newCategory;
                    } catch (LibraryException e) {
                        System.err.println("Error: " + e.getMessage());
                        return null;
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
            System.out.print("Enter number: ");
            try {
                int pick = Integer.parseInt(scanner.nextLine()) - 1;
                return categories.get(pick);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input");
                return null;
            }
        }
    }
}