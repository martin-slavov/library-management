package com.github.martinslavov.service;

import com.github.martinslavov.dao.AuthorDAO;
import com.github.martinslavov.dao.BookDAO;
import com.github.martinslavov.exception.LibraryException;
import com.github.martinslavov.model.Author;
import com.github.martinslavov.model.Book;
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
class AuthorServiceTest {

    @Mock
    AuthorDAO authorDAO;

    @Mock
    BookDAO bookDAO;

    @InjectMocks
    AuthorService authorService;

    private Author validAuthor;

    @BeforeEach
    void setUp() {
        validAuthor = new Author("Frank", "Herbert", "American");
    }

    @Test
    @DisplayName("Add author with valid data should save author")
    void addAuthor_withValidData_shouldSaveAuthor() {
        when(authorDAO.findByFullName(validAuthor.getFirstName(), validAuthor.getLastName())).thenReturn(Collections.emptyList());

        authorService.addAuthor(validAuthor);

        verify(authorDAO).save(validAuthor);
    }

    @Test
    @DisplayName("Add author with duplicate author should throw LibraryException")
    void addAuthor_withDuplicatedAuthor_shouldThrowLibraryException() {
        when(authorDAO.findByFullName(validAuthor.getFirstName(), validAuthor.getLastName())).thenReturn(List.of(new Author("Frank", "Herbert", "American")));

        assertThrows(LibraryException.class, () -> authorService.addAuthor(validAuthor));
    }

    @Test
    @DisplayName("Remove author with valid data should delete author")
    void removeAuthor_withValidData_shouldDeleteAuthor() {
        when(authorDAO.findById(validAuthor.getAuthorId())).thenReturn(Optional.of(new Author("Frank", "Herbert", "American")));
        when(bookDAO.findByAuthorId(validAuthor.getAuthorId())).thenReturn(Collections.emptyList());

        authorService.removeAuthor(validAuthor);

        verify(authorDAO).delete(validAuthor.getAuthorId());
    }

    @Test
    @DisplayName("Remove author with author not found should throw LibraryException")
    void removeAuthor_withAuthorNotFound_shouldThrowLibraryException() {
        when(authorDAO.findById(validAuthor.getAuthorId())).thenReturn(Optional.empty());

        assertThrows(LibraryException.class, () -> authorService.removeAuthor(validAuthor));
    }

    @Test
    @DisplayName("Remove author with books in catalog throw LibraryException")
    void removeAuthor_withBooksInCatalog_shouldThrowLibraryException() {
        when(authorDAO.findById(validAuthor.getAuthorId())).thenReturn(Optional.of(new Author("Frank", "Herbert", "American")));
        when(bookDAO.findByAuthorId(validAuthor.getAuthorId())).thenReturn(List.of(new Book("9780441172719", "Dune", 1, 1, 1965, 3, 3)));

        assertThrows(LibraryException.class, () -> authorService.removeAuthor(validAuthor));
    }
}