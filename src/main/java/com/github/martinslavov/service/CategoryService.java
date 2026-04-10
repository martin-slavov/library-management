package com.github.martinslavov.service;

import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.dao.CategoryDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Category;

public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final BookDAO bookDAO;

    public CategoryService(CategoryDAO categoryDAO, BookDAO bookDAO) {
        this.categoryDAO = categoryDAO;
        this.bookDAO = bookDAO;
    }

    public void addCategory(Category category) {

        if (!categoryDAO.findByName(category.getName()).isEmpty()) {
            throw new LibraryException("Category already exists");
        }
        categoryDAO.save(category);
    }

    public void removeCategory(Category category) {

        if (categoryDAO.findById(category.getCategoryId()).isEmpty()) {
            throw new LibraryException("Category not found");
        }

        if (!bookDAO.findByCategoryId(category.getCategoryId()).isEmpty()) {
            throw new LibraryException("Cannot remove category — books are assigned to it");
        }
        categoryDAO.delete(category.getCategoryId());
    }
}
