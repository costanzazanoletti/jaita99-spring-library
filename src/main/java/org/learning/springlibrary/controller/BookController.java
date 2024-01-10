package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  // metodo create che mostra la pagina col form di creazione di un Book
  @GetMapping("/create")
  public String create(Model model) {
    // passo tramite Model un attributo di tipo Book vuoto
    model.addAttribute("book", new Book());
    return "books/create";
  }

  // metodo che riceve il submit del form di creazione e salva su db il Book
  // per attivare la validazione sul formBook lo annoto come @Valid e gli errori verranno
  // inseriti nella mappa BindingResult, che deve essere il parametro immmediatamente successivo
  @PostMapping("/create")
  public String store(@Valid @ModelAttribute("book") Book formBook, BindingResult bindingResult) {
    // valido i dati del Book, cioè verifico se la mappa BindingResult ha errori
    if (bindingResult.hasErrors()) {
      // qui gestisco che ho campi non validi
      // ricaricando il template del form
      return "books/create";
    }

    // se sono validi lo salvo su db
    formBook.setCreatedAt(LocalDateTime.now());
    Book savedBook = bookRepository.save(formBook);
    // faccio una redirect alla pagina di dettaglio del libro appena creato
    return "redirect:/books/show/" + savedBook.getId();
  }

}
