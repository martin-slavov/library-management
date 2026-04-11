package com.github.martinslavov.util;

public class MenuConstants {

    public static final String MAIN_MENU = """
            === Library Management System ===
            1. Books
            2. Members
            3. Authors
            4. Categories
            5. Loans
            6. Fines
            0. Quit
            
            Enter your choice:\s
            """;

    public static final String BOOKS_MENU = """
            === Books ===
            1. Add book
            2. Remove book
            3. Search book
            4. Update book
            0. Back
            
            Enter your choice:\s
            """;

    public static final String BOOKS_SEARCH_MENU = """
            === Search Books ===
            1. All books
            2. By ISBN
            3. By title
            4. By author
            5. By category
            0. Back
            
            Enter your choice:\s
            """;
    public static final String BOOKS_UPDATE_MENU = """
            === Update Book (ISBN: %s) ===
            1. Update title
            2. Update author
            3. Update category
            4. Add copies
            0. Back
            
            Enter your choice:\s
            """;

    public static final String MEMBER_MENU = """
            === Members ===
            1. Register member
            2. Suspend member
            3. Renew membership
            4. Search member
            5. Update member
            0. Back
            
            Enter your choice:\s
            """;

    public static final String MEMBER_SEARCH_MENU = """
            === Search Members ===
            1. All members
            2. By first name
            3. By last name
            4. By phone
            5. By email
            6. By status
            0. Back
            
            Enter your choice:\s
            """;

    public static final String AUTHOR_MENU = """
            === Authors ===
            1. Add author
            2. Remove author
            3. Search author
            4. Update author
            0. Back
            
            Enter your choice:\s
            """;

    public static final String AUTHOR_SEARCH_MENU = """
            === Search Authors ===
            1. All authors
            2. By first name
            3. By last name
            4. By full name
            5. By nationality
            0. Back
            
            Enter your choice:\s
            """;

    public static final String CATEGORY_MENU = """
            === Categories ===
            1. Add category
            2. Remove category
            3. Search category
            0. Back
            
            Enter your choice:\s
            """;

    public static final String CATEGORY_SEARCH_MENU = """
            === Search Categories ===
            1. All categories
            2. By name
            0. Back
            
            Enter your choice:\s
            """;

    public static final String LOAN_MENU = """
            === Loans ===
            1. Borrow book
            2. Return book
            3. Get member loan history
            4. Search loan
            0. Back
            
            Enter your choice:\s
            """;

    public static final String LOAN_SEARCH_MENU = """
            === Search Loans ===
            1. All loans
            2. By book
            3. By member
            4. By status
            0. Back
            
            Enter your choice:\s
            """;

    public static final String FINE_MENU = """
            === Fines ===
            1. Pay fine
            2. Waive fine
            3. Search fine
            0. Back
            
            Enter your choice:\s
            """;

    public static final String FINE_SEARCH_MENU = """
            === Search Fines ===
            1. All fines
            2. By status
            0. Back
            
            Enter your choice:\s
            """;
}
