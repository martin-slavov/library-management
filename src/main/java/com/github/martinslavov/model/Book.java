package com.github.martinslavov.model;

public class Book {

    private int bookId;
    private String isbn;
    private String title;
    private int authorId;
    private int categoryId;
    private final int publicationDate;
    private int totalCopies;
    private int availableCopies;

    public Book(int bookId, String isbn, String title, int authorId, int categoryId, int publicationDate, int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.publicationDate = publicationDate;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public Book(String isbn, String title, int authorId, int categoryId, int publicationDate, int totalCopies, int availableCopies) {
        this(-1, isbn, title, authorId, categoryId, publicationDate, totalCopies, availableCopies);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPublicationDate() {
        return publicationDate;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                ", categoryId=" + categoryId +
                ", publicationDate=" + publicationDate +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                '}';
    }
}
