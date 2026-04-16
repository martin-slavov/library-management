package com.github.martinslavov.service;

import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.dao.CategoryDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryDAO categoryDAO;

    @Mock
    BookDAO bookDAO;

    @InjectMocks
    CategoryService categoryService;

    private Category validCategory;

    @BeforeEach
    void setUp() {
        validCategory = new Category("Science Fiction", "Sci-fi books");
    }

    @Test
    @DisplayName("Add category with valid data should save category")
    void addCategory_withValidData_shouldSaveCategory() {
        when(categoryDAO.findByName(validCategory.getName())).thenReturn(Collections.emptyList());

        categoryService.addCategory(validCategory);

        verify(categoryDAO).save(validCategory);
    }

    @Test
    @DisplayName("Add category with duplicate category should throw LibraryException")
    void addCategory_withDuplicateCategory_shouldThrowLibraryException() {
        when(categoryDAO.findByName(validCategory.getName())).thenReturn(List.of(new Category("Science Fiction", "Sci-fi books")));

        assertThrows(LibraryException.class, () -> categoryService.addCategory(validCategory));
    }

    @Test
    @DisplayName("Remove category with valid data should delete category")
    void removeCategory_withValidData_shouldDeleteCategory() {
        when(categoryDAO.findById(validCategory.getCategoryId())).thenReturn(Optional.of(validCategory));
        when(bookDAO.findByCategoryId(validCategory.getCategoryId())).thenReturn(Collections.emptyList());

        categoryService.removeCategory(validCategory);

        verify(categoryDAO).delete(validCategory.getCategoryId());
    }

    @Test
    @DisplayName("Remove category with category not found should throw LibraryException")
    void removeCategory_withCategoryNotFound_shouldThrowLibraryException() {
        when(categoryDAO.findById(validCategory.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> categoryService.removeCategory(validCategory));
    }

    @Test
    @DisplayName("Remove category with book in catalog should throw LibraryException")
    void removeCategory_withBooksInCatalog_shouldThrowLibraryException() {
        when(categoryDAO.findById(validCategory.getCategoryId())).thenReturn(Optional.of(validCategory));
        when(bookDAO.findByCategoryId(validCategory.getCategoryId())).thenReturn(List.of(new Book("9780441172719", "Dune", 1, 1, 1965, 3, 3)));

        assertThrows(LibraryException.class, () -> categoryService.removeCategory(validCategory));
    }
}