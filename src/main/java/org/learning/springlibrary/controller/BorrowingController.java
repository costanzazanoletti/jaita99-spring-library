package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
  public String store(@Valid @ModelAttribute("borrowing") Borrowing formBorrowing,
      BindingResult bindingResult, Model model) {
    // valido l'oggetto
    if (bindingResult.hasErrors()) {
      // se ci sono errori ritorno il template del form
      model.addAttribute("book", formBorrowing.getBook());
      return "borrowings/create";
    }
    if (formBorrowing.getExpireDate() != null && formBorrowing.getExpireDate()
        .isBefore(formBorrowing.getStartDate())) {
      formBorrowing.setExpireDate(formBorrowing.getStartDate().plusDays(30));
    }
    // se non ci sono errori lo salvo su database
    Borrowing storedBorrowing = borrowingRepository.save(formBorrowing);
    // faccio una redirect alla pagina di dettaglio del libro
    return "redirect:/books/show/" + storedBorrowing.getBook().getId();
  }

  // metodo per restituire la pagina col form di modifica
  @GetMapping("/edit/{id}") // http://localhost:8080/borrowings/edit/1
  public String edit(@PathVariable Integer id, Model model) {
    // recupero il Borrowing con quell'id da database
    Optional<Borrowing> result = borrowingRepository.findById(id);
    // se è presente precarico il form con il Borrowing
    if (result.isPresent()) {
      // lo passo come attributo al Model
      model.addAttribute("borrowing", result.get());
      // restituisco il template
      return "borrowings/edit";
    } else {
      // altrimenti sollevo un'eccezione HTTP 404
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrowing with id " + id
          + " not found");

    }
  }

  // metodo per ricevere il submit del form di edit
  @PostMapping("/edit/{id}") // http://localhost:8080/borrowings/edit/1
  public String update(@PathVariable Integer id,
      @Valid @ModelAttribute("borrowing") Borrowing formBorrowing, BindingResult bindingResult) {
    // valido il Borrowing
    if (bindingResult.hasErrors()) {
      // se ci sono errori ricarico la pagina col form di edit
      return "borrowings/edit";
    }
    // se non ci sono errori lo salvo su db
    Borrowing updatedBorrowing = borrowingRepository.save(formBorrowing);
    // redirect al dettaglio del libro prestato
    return "redirect:/books/show/" + updatedBorrowing.getBook().getId();
  }

}
