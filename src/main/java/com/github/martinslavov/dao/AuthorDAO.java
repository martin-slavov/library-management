package com.github.martinslavov.dao;

import com.github.martinslavov.config.DatabaseConnection;
import com.github.martinslavov.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAO {

    public Optional<Author> save(Author author) {

        String sql = "INSERT INTO authors (first_name, last_name, nationality) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psInsertAuthor = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            psInsertAuthor.setString(1, author.getFirstName());
            psInsertAuthor.setString(2, author.getLastName());
            psInsertAuthor.setString(3, author.getNationality());

            psInsertAuthor.executeUpdate();

            ResultSet generatedKeys = psInsertAuthor.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setAuthorId(generatedKeys.getInt(1));
            }

            return Optional.of(author);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Author> findById(int id) {

        String sql = "SELECT * FROM authors WHERE author_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindById = conn.prepareStatement(sql)
        ) {
            psFindById.setInt(1, id);

            ResultSet rs = psFindById.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToAuthor(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Author> findAll() {

        String sql = "SELECT * FROM authors";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindAll = conn.prepareStatement(sql)
        ) {
            ResultSet rs = psFindAll.executeQuery();
            List<Author> allAuthors = new ArrayList<>();
            while (rs.next()) {
                allAuthors.add(mapResultSetToAuthor(rs));
            }
            return allAuthors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Author> findByFirstName(String firstName) {

        String sql = "SELECT * FROM authors WHERE first_name LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByFirstName = conn.prepareStatement(sql)
        ) {
            psFindByFirstName.setString(1, "%" + firstName + "%");
            ResultSet rs = psFindByFirstName.executeQuery();
            List<Author> authorByFirstName = new ArrayList<>();
            while (rs.next()) {
                authorByFirstName.add(mapResultSetToAuthor(rs));
            }
            return authorByFirstName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Author> findByLastName(String lastName) {

        String sql = "SELECT * FROM authors WHERE last_name LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByLastName = conn.prepareStatement(sql)
        ) {
            psFindByLastName.setString(1, "%" + lastName + "%");
            ResultSet rs = psFindByLastName.executeQuery();
            List<Author> authorByLastName = new ArrayList<>();
            while (rs.next()) {
                authorByLastName.add(mapResultSetToAuthor(rs));
            }
            return authorByLastName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Author> findByFullName(String firstName, String lastName) {

        String sql = "SELECT * FROM authors WHERE first_name = ? AND last_name = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByFullName = conn.prepareStatement(sql)
        ) {
            psFindByFullName.setString(1, firstName);
            psFindByFullName.setString(2, lastName);
            ResultSet rs = psFindByFullName.executeQuery();
            List<Author> authorByFullName = new ArrayList<>();
            while (rs.next()) {
                authorByFullName.add(mapResultSetToAuthor(rs));
            }
            return authorByFullName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Author> findByNationality(String nationality) {

        String sql = "SELECT * FROM authors WHERE nationality LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByNationality = conn.prepareStatement(sql)
        ) {
            psFindByNationality.setString(1, "%" + nationality + "%");
            ResultSet rs = psFindByNationality.executeQuery();
            List<Author> authorByNationality = new ArrayList<>();
            while (rs.next()) {
                authorByNationality.add(mapResultSetToAuthor(rs));
            }
            return authorByNationality;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Author author) {

        String sql = "UPDATE authors SET first_name = ?, last_name = ?, nationality = ? WHERE author_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psUpdateAuthor = conn.prepareStatement(sql)
        ) {
            psUpdateAuthor.setString(1, author.getFirstName());
            psUpdateAuthor.setString(2, author.getLastName());
            psUpdateAuthor.setString(3, author.getNationality());
            psUpdateAuthor.setInt(4, author.getAuthorId());

            return psUpdateAuthor.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM authors WHERE author_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psDeleteAuthor = conn.prepareStatement(sql)
        ) {
            psDeleteAuthor.setInt(1, id);
            return psDeleteAuthor.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException {
        return new Author(
                rs.getInt("author_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("nationality")
        );
    }
}
