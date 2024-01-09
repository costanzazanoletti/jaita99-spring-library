package org.learning.springlibrary.controller;

import java.util.List;
import java.util.Optional;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/books")
public class BookController {

  // il Controller ha bisogno delle funzionalità del Repository
  @Autowired
  private BookRepository bookRepository;

  // metodo index che mostra la lista di tutti i libri
  @GetMapping
  public String index(Model model) {
    // recupero la lista di libri dal database
    List<Book> bookList = bookRepository.findAll();
    // aggiungo la lista di libri agli attributi del Model
    model.addAttribute("bookList", bookList);
    return "books/list";
  }

  // metodo show che mostra i dettagli di un singolo libro
  @GetMapping("/show/{id}") // /books/show/{id}
  public String show(@PathVariable Integer id, Model model) {
    // nel corpo del metodo ho l'id del book da cercare
    Optional<Book> result = bookRepository.findById(id);
    // verifico se il Book è stato trovato
    if (result.isPresent()) {
      // estraggo il Book dall'Optional
      Book book = result.get();
      // aggiungo al Model l'attributo con il Book
      model.addAttribute("book", book);
      // restituisco il template
      return "books/show";
    } else {
      // gestisco il caso in cui nel database un Book con quell'id non c'è
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " not found");
    }
  }


}
