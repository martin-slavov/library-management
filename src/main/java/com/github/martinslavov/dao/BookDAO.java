package com.github.martinslavov.dao;

import com.github.martinslavov.config.DatabaseConnection;
import com.github.martinslavov.exception.DatabaseException;
import com.github.martinslavov.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO {

    public Optional<Book> save(Book book) {

        String sql = "INSERT INTO books (ISBN, title, author_id, category_id, publication_date, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psInsertBook = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            psInsertBook.setString(1, book.getIsbn());
            psInsertBook.setString(2, book.getTitle());
            psInsertBook.setInt(3, book.getAuthorId());
            psInsertBook.setInt(4, book.getCategoryId());
            psInsertBook.setInt(5, book.getPublicationDate());
            psInsertBook.setInt(6, book.getTotalCopies());
            psInsertBook.setInt(7, book.getAvailableCopies());

            psInsertBook.executeUpdate();

            ResultSet generatedKeys = psInsertBook.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setBookId(generatedKeys.getInt(1));
            }

            return Optional.of(book);

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save book", e);
        }
    }

    public List<Book> findAll() {

        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindAll = conn.prepareStatement(sql)
        ) {
            ResultSet rs = psFindAll.executeQuery();
            List<Book> allBooks = new ArrayList<>();
            while (rs.next()) {
                allBooks.add(mapResultSetToBook(rs));
            }
            return allBooks;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve all books", e);
        }
    }

    public Optional<Book> findById(int id) {

        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindById = conn.prepareStatement(sql)
        ) {
            psFindById.setInt(1, id);

            ResultSet rs = psFindById.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find book by ID", e);
        }
    }

    public Optional<Book> findByIsbn(String isbn) {

        String sql = "SELECT * FROM books WHERE ISBN = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByIsbn = conn.prepareStatement(sql)
        ) {
            psFindByIsbn.setString(1, isbn);
            ResultSet rs = psFindByIsbn.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find book by ISBN", e);
        }
    }

    public List<Book> findByTitle(String title) {

        String sql = "SELECT * FROM books WHERE title LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByTitle = conn.prepareStatement(sql)
        ) {
            psFindByTitle.setString(1, "%" + title + "%");
            ResultSet rs = psFindByTitle.executeQuery();
            List<Book> bookByTitle = new ArrayList<>();
            while (rs.next()) {
                bookByTitle.add(mapResultSetToBook(rs));
            }
            return bookByTitle;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find book by title", e);
        }
    }

    public List<Book> findByAuthorId(int authorId) {

        String sql = "SELECT * FROM books WHERE author_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByAuthorId = conn.prepareStatement(sql)
        ) {
            psFindByAuthorId.setInt(1, authorId);
            ResultSet rs = psFindByAuthorId.executeQuery();
            List<Book> bookByAuthorId = new ArrayList<>();
            while (rs.next()) {
                bookByAuthorId.add(mapResultSetToBook(rs));
            }
            return bookByAuthorId;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find book by author ID", e);
        }
    }

    public List<Book> findByCategoryId(int categoryId) {

        String sql = "SELECT * FROM books WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByCategoryId = conn.prepareStatement(sql)
        ) {
            psFindByCategoryId.setInt(1, categoryId);
            ResultSet rs = psFindByCategoryId.executeQuery();
            List<Book> bookByCategoryId = new ArrayList<>();
            while (rs.next()) {
                bookByCategoryId.add(mapResultSetToBook(rs));
            }
            return bookByCategoryId;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find book by category ID", e);
        }
    }

    public boolean update(Book book) {

        String sql = """
                UPDATE books SET ISBN = ?, title = ?, author_id = ?, category_id = ?,\s
                publication_date = ?, total_copies = ?, available_copies = ?\s
                WHERE book_id = ?""";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psUpdateBook = conn.prepareStatement(sql)
        ) {
            psUpdateBook.setString(1, book.getIsbn());
            psUpdateBook.setString(2, book.getTitle());
            psUpdateBook.setInt(3, book.getAuthorId());
            psUpdateBook.setInt(4, book.getCategoryId());
            psUpdateBook.setInt(5, book.getPublicationDate());
            psUpdateBook.setInt(6, book.getTotalCopies());
            psUpdateBook.setInt(7, book.getAvailableCopies());
            psUpdateBook.setInt(8, book.getBookId());

            return psUpdateBook.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update book", e);
        }
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psDeleteBook = conn.prepareStatement(sql)
        ) {
            psDeleteBook.setInt(1, id);
            return psDeleteBook.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete book", e);
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("book_id"),
                rs.getString("ISBN"),
                rs.getString("title"),
                rs.getInt("author_id"),
                rs.getInt("category_id"),
                rs.getInt("publication_date"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies"));
    }
}
