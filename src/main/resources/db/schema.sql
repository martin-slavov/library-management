CREATE DATABASE IF NOT EXISTS library_db ;
USE library_db ;

CREATE TABLE Authors (
    author_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    nationality VARCHAR(50)
);

CREATE TABLE Categories (
    category_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL
);

CREATE TABLE Books (
    book_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    ISBN VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(50) NOT NULL,
    author_id INT NOT NULL,
    category_id INT NOT NULL,
    publication_date INT NOT NULL,
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id)
        REFERENCES Authors (author_id),
    CONSTRAINT fk_category FOREIGN KEY (category_id)
        REFERENCES Categories (category_id)
);

CREATE TABLE Members (
    member_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    expire_date DATE NOT NULL,
    status ENUM('ACTIVE', 'SUSPENDED', 'EXPIRED') NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE Loans (
    loan_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    return_date DATE,
    status ENUM('ACTIVE', 'RETURNED', 'OVERDUE') NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_book FOREIGN KEY (book_id)
        REFERENCES Books (book_id),
    CONSTRAINT fk_member FOREIGN KEY (member_id)
        REFERENCES Members (member_id)
);

CREATE TABLE Fines (
    fine_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    loan_id INT NOT NULL,
    amount DECIMAL(10 , 2 ) NOT NULL,
    status ENUM('UNPAID', 'PAID', 'WAIVED') NOT NULL DEFAULT 'UNPAID',
    CONSTRAINT fk_loan FOREIGN KEY (loan_id)
        REFERENCES Loans (loan_id)
);

INSERT INTO Authors (first_name, last_name, nationality) 
VALUES ('Frank', 'Herbert', 'American'), 
       ('Andrzej', 'Sapkowski', 'Polish'),
       ('George', 'Orwell', 'British');

INSERT INTO Categories (name, description) 
VALUES ('Science Fiction', 'Speculative fiction involving futuristic concepts.'),
       ('Fantasy', 'Magical and supernatural elements in imaginary worlds.'),
       ('Dystopian', 'Exploration of social and political structures.');

INSERT INTO Books (ISBN, title, author_id, category_id, publication_date, total_copies, available_copies) 
VALUES ('978-0441172719', 'Dune', 1, 1, 1965, 3, 3),
       ('978-0575082441', 'The Witcher: The Last Wish', 2, 2, 1993, 2, 2),
       ('978-0451524935', '1984', 3, 3, 1949, 5, 5);

INSERT INTO Members (first_name, last_name, phone, email, start_date, expire_date, status) 
VALUES ('Ivan', 'Doichinov', '0888111222', 'doichinov94@gmail.com', '2024-11-05', '2025-11-05', 'EXPIRED'),
       ('Georgi', 'Ivanov', '0887658997', 'georgi.ivanov@gmail.com', '2025-09-04', '2026-09-04', 'ACTIVE'),
       ('Maria', 'Petrova', '0899123456', 'maria.p@abv.bg', '2025-10-23', '2026-10-23', 'ACTIVE');

INSERT INTO Loans (book_id, member_id, start_date, end_date, return_date, status)
VALUES (1, 2, '2025-09-10', '2025-10-10', '2025-10-04','RETURNED'),
	   (1, 1, '2024-11-07', '2024-12-07', '2024-12-06','RETURNED');

INSERT INTO Loans (book_id, member_id, start_date, end_date, status)
VALUES (2, 3, '2025-10-24', '2025-11-24', 'OVERDUE');

INSERT INTO Fines (loan_id, amount, status)
VALUES (2, 12.50, 'UNPAID');