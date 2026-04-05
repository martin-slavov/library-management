package com.github.martinslavov.dao;

import com.github.martinslavov.config.DatabaseConnection;
import com.github.martinslavov.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {

    public Optional<Category> save(Category category) {

        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psInsertCategory = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            psInsertCategory.setString(1, category.getName());
            psInsertCategory.setString(2, category.getDescription());

            psInsertCategory.executeUpdate();

            ResultSet generatedKeys = psInsertCategory.getGeneratedKeys();
            if (generatedKeys.next()) {
                category.setCategoryId(generatedKeys.getInt(1));
            }

            return Optional.of(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> findAll() {

        String sql = "SELECT * FROM categories";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindAll = conn.prepareStatement(sql)
        ) {
            ResultSet rs = psFindAll.executeQuery();
            List<Category> allCategories = new ArrayList<>();
            while (rs.next()) {
                allCategories.add(mapResultSetToCategory(rs));
            }
            return allCategories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Category> findById(int id) {

        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindById = conn.prepareStatement(sql)
        ) {
            psFindById.setInt(1, id);

            ResultSet rs = psFindById.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCategory(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> findByName(String name) {

        String sql = "SELECT * FROM categories WHERE name LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psFindByName = conn.prepareStatement(sql)
        ) {
            psFindByName.setString(1, "%" + name + "%");
            ResultSet rs = psFindByName.executeQuery();
            List<Category> categoriesByName = new ArrayList<>();
            while (rs.next()) {
                categoriesByName.add(mapResultSetToCategory(rs));
            }
            return categoriesByName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Category category) {

        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psUpdateCategory = conn.prepareStatement(sql)
        ) {
            psUpdateCategory.setString(1, category.getName());
            psUpdateCategory.setString(2, category.getDescription());
            psUpdateCategory.setInt(3, category.getCategoryId());

            return psUpdateCategory.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psDeleteCategory = conn.prepareStatement(sql)
        ) {
            psDeleteCategory.setInt(1, id);
            return psDeleteCategory.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description")
        );
    }
}
