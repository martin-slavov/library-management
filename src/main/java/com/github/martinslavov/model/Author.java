package com.github.martinslavov.model;

public class Author {

    private int authorId;
    private String firstName;
    private String lastName;
    private String nationality;

    public Author(int authorId, String firstName, String lastName, String nationality) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }

    public Author(String firstName, String lastName, String nationality) {
        this(-1, firstName, lastName, nationality);
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
