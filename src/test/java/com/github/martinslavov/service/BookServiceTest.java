package com.github.martinslavov.service;

import com.github.martinslavov.dao.AuthorDAO;
import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.dao.CategoryDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.model.Book;
import com.github.martinslavov.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookDAO bookDAO;

    @Mock
    AuthorDAO authorDAO;

    @Mock
    CategoryDAO categoryDAO;

    @InjectMocks
    BookService bookService;

    private Book validBook;

    @BeforeEach
    void setUp() {
        validBook = new Book("9780441172719", "Dune", 1, 1, 1965, 3, 3);
    }

    @Test
    @DisplayName("Add book with valid data should save book")
    void addBook_withValidData_shouldSaveBook() {
        when(bookDAO.findByIsbn(validBook.getIsbn())).thenReturn(Optional.empty());
        when(authorDAO.findById(validBook.getAuthorId())).thenReturn(Optional.of(new Author("Frank", "Herbert", "American")));
        when(categoryDAO.findById(validBook.getCategoryId())).thenReturn(Optional.of(new Category("Science Fiction", "Sci-fi books")));

        bookService.addBook(validBook);

        verify(bookDAO).save(validBook);
    }

    @Test
    @DisplayName("Add book with invalid ISBN format should throw LibraryException")
    void addBook_withInvalidIsbnFormat_shouldThrowLibraryException() {
        Book book = new Book("abc", "Dune", 1, 1, 1965, 3, 3);

        assertThrows(LibraryException.class, () -> bookService.addBook(book));
    }

    @Test
    @DisplayName("Add book with duplicate ISBN should throw LibraryException")
    void addBook_withDuplicateIsbn_shouldThrowLibraryException() {
        when(bookDAO.findByIsbn(validBook.getIsbn())).thenReturn(Optional.of(validBook));

        assertThrows(LibraryException.class, () -> bookService.addBook(validBook));
    }

    @Test
    @DisplayName("Add book with author not found should throw LibraryException")
    void addBook_withAuthorNotFound_shouldThrowLibraryException() {
        when(bookDAO.findByIsbn(validBook.getIsbn())).thenReturn(Optional.empty());
        when(authorDAO.findById(validBook.getAuthorId())).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> bookService.addBook(validBook));
    }

    @Test
    @DisplayName("Add book with category not found should throw LibraryException")
    void addBook_withCategoryNotFound_shouldThrowLibraryException() {
        when(bookDAO.findByIsbn(validBook.getIsbn())).thenReturn(Optional.empty());
        when(authorDAO.findById(validBook.getAuthorId())).thenReturn(Optional.of(new Author("Frank", "Herbert", "American")));
        when(categoryDAO.findById(validBook.getCategoryId())).thenReturn(Optional.empty());


        assertThrows(LibraryException.class, () -> bookService.addBook(validBook));
    }

    @Test
    @DisplayName("Add book with zero total copies should throw LibraryException")
    void addBook_withZeroTotalCopies_shouldThrowLibraryException() {
        Book book = new Book("9780441172719", "Dune", 1, 1, 1965, 0, 0);
        when(bookDAO.findByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        when(authorDAO.findById(book.getAuthorId())).thenReturn(Optional.of(new Author("Frank", "Herbert", "American")));
        when(categoryDAO.findById(book.getCategoryId())).thenReturn(Optional.of(new Category("Science Fiction", "Sci-fi books")));

        assertThrows(LibraryException.class, () -> bookService.addBook(book));
    }

    @Test
    @DisplayName("Remove book with valid data should delete book")
    void removeBook_withValidData_shouldDeleteBook() {
        when(bookDAO.findById(validBook.getBookId())).thenReturn(Optional.of(validBook));

        bookService.removeBook(validBook);

        verify(bookDAO).delete(validBook.getBookId());
    }

    @Test
    @DisplayName("Remove book with book not found should throw LibraryException")
    void removeBook_withBookNotFound_shouldDeleteBook() {
        when(bookDAO.findById(validBook.getBookId())).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> bookService.removeBook(validBook));
    }

    @Test
    @DisplayName("Remove book with copies on loan should throw LibraryException")
    void removeBook_withCopiesOnLoan_shouldDeleteBook() {
        Book bookWithLoan = new Book("9780441172719", "Dune", 1, 1, 1965, 3, 2);
        when(bookDAO.findById(bookWithLoan.getBookId())).thenReturn(Optional.of(bookWithLoan));

        assertThrows(LibraryException.class, () -> bookService.removeBook(bookWithLoan));
    }
}