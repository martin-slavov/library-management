# Library Management System

A command-line Java application for managing a library's books, members, loans, and fines. Built as a portfolio project
demonstrating layered architecture, design patterns, and database integration.

## Features

- **Book management** — add, remove, search, and update books with ISBN validation
- **Member management** — register members, suspend, renew memberships with email and phone validation
- **Loan management** — borrow and return books with automatic fine calculation for overdue returns
- **Fine management** — pay and waive fines
- **Author and category management** — full CRUD operations
- **Background overdue scanner** — automatically marks overdue loans and calculates fines every 24 hours

## Tech Stack

- Java 21
- MySQL 8
- HikariCP 7
- JUnit 5
- Mockito 5
- Maven

## Prerequisites

- Java 21+
- MySQL 8+
- Maven 3.8+

## Setup

1. Clone the repository:

```bash
   git clone https://github.com/martinslavov/library-management.git
```

2. Create the database by running the SQL scripts in order:

```bash
   mysql -u root -p < src/main/resources/db/schema.sql
   mysql -u root -p < src/main/resources/db/procedures.sql
```

3. Create `src/main/resources/db.properties` with your database credentials:

```properties
db.url=jdbc:mysql://localhost:3306/library_db
db.username=your_username
db.password=your_password
```

4. Build and run:

```bash
   mvn compile exec:java -Dexec.mainClass="com.github.martinslavov.Main"
```

## Project Structure

```text
src/main/java/com/github/martinslavov/
├── commands/        — Command Pattern implementation
├── config/          — Database connection (Singleton)
├── dao/             — Data Access Objects
├── exception/       — Custom exceptions
├── model/           — Domain model classes
├── scheduler/       — Background overdue scanner
├── service/         — Business logic layer
└── util/            — Helper classes
```

## Architecture

The application follows a layered architecture with clear separation of concerns. The **Command Pattern** encapsulates
each user action as an independent class, making the codebase easy to extend. Database access is abstracted through the
**DAO Pattern**, and a single **HikariCP connection pool** (Singleton) manages all database connections efficiently.

## Testing

Unit tests covering all service layer business logic, written with JUnit 5 and Mockito.

```bash
mvn test
```