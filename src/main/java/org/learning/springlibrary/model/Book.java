package org.learning.springlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

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
  @Column(nullable = false, length = 13, unique = true)
  private String isbn;
  private Integer year;
  @Lob
  private String synopsis;
  private LocalDateTime createdAt;

  @NotNull
  @Min(1)
  private int numberOfCopies;


  // attributo che rappresenta i prestiti
  @OneToMany(mappedBy = "book")
  // no nuova relazione, l'avevo già definita sull'attributo book di Borrowing
  private List<Borrowing> borrowings;

  // COSTRUTTORI (se ne creo uno con parametri devo anche crearne uno vuoto)

  // METODI (devo assolutamente mettere tutti i getter e setter)

  // metodo per calcolare il numero di copie in prestito (ATTRIBUTO DERIVATO O CALCOLATO)
  public int getAvailableCopies() {
    // sottraggo al numero di copie totale il numero di copie in prestito

    int borrowedCopies = 0;
    for (Borrowing borrowing : borrowings) {
      if (borrowing.getReturnDate() == null) {
        borrowedCopies++;
      }
    }
    return numberOfCopies - borrowedCopies;
  }

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

  public int getNumberOfCopies() {
    return numberOfCopies;
  }

  public void setNumberOfCopies(int numberOfCopies) {
    this.numberOfCopies = numberOfCopies;
  }

  public List<Borrowing> getBorrowings() {
    return borrowings;
  }

  public void setBorrowings(List<Borrowing> borrowings) {
    this.borrowings = borrowings;
  }
}
