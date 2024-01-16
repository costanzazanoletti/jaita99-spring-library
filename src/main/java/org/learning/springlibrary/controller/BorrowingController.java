package org.learning.springlibrary.controller;

import java.time.LocalDate;
import java.util.Optional;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.model.Borrowing;
import org.learning.springlibrary.repository.BookRepository;
import org.learning.springlibrary.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/borrowings")
public class BorrowingController {

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BorrowingRepository borrowingRepository;


  // metodo per mostrare la pagina col form di creazione di un Borrowing
  @GetMapping("/create")
  public String create(@RequestParam(name = "bookId", required = true) Integer bookId,
      Model model) {
    // verifico se il book esiste su database
    Optional<Book> result = bookRepository.findById(bookId);
    if (result.isPresent()) {
      // estraggo il Book dall'Optional
      Book bookToBorrow = result.get();
      // passo al Model il Book come attributo
      model.addAttribute("book", bookToBorrow);
      // preparo il Borrowing per precaricare il form
      Borrowing newBorrowing = new Borrowing();
      // precarico i campi book, startDate e expireDate
      newBorrowing.setBook(bookToBorrow);
      newBorrowing.setStartDate(LocalDate.now());
      newBorrowing.setExpireDate(LocalDate.now().plusDays(30));
      model.addAttribute("borrowing", newBorrowing);
      // restituisco il template
      return "borrowings/create";
    } else {
      // se l'Optional è vuoto sollevo una eccezione HTTP 404
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Book with id " + bookId + " not found");
    }
  }

  @PostMapping("/create")
  public String store(Borrowing formBorrowing) {
    // valido l'oggetto

    // se ci sono errori ritorno il template del form

    // se non ci sono errori lo salvo su database
    Borrowing storedBorrowing = borrowingRepository.save(formBorrowing);
    // faccio una redirect alla pagina di dettaglio del libro
    return "redirect:/books/show/" + storedBorrowing.getBook().getId();
  }

}
