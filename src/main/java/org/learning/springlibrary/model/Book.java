package org.learning.springlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

  // ATTRIBUTI
  @Id // primary key
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
  private Integer id;
  @NotEmpty(message = "Title must not be blank")
  @Column(nullable = false)
  private String title;
  @NotEmpty(message = "Authors must not be blank")
  private String authors;
  private String publisher;
  @NotEmpty(message = "ISBN must not be blank")
  @Size(min = 10, max = 13, message = "ISBN size must be from 10 to 13 characters")
  @Column(nullable = false, length = 13)
  private String isbn;
  private Integer year;
  @Lob
  private String synopsis;
  private LocalDateTime createdAt;

  // COSTRUTTORI (se ne creo uno con parametri devo anche crearne uno vuoto)

  // METODI (devo assolutamente mettere tutti i getter e setter)

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
