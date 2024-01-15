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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

  // il Controller ha bisogno delle funzionalità del Repository
  @Autowired
  private BookRepository bookRepository;

  // metodo index che mostra la lista di tutti i libri
  @GetMapping
  public String index(@RequestParam(name = "keyword", required = false) String searchKeyword,
      Model model) {
    List<Book> bookList;
    // se searchKeyword è presente faccio la ricerca per titolo
    if (searchKeyword != null) {
      bookList = bookRepository.findByTitleContainingOrAuthorsContaining(searchKeyword,
          searchKeyword);
    } else {
      // altrimenti recupero la lista di tutti i libri dal database
      bookList = bookRepository.findAll();
    }
    // aggiungo la lista di libri agli attributi del Model
    model.addAttribute("bookList", bookList);
    // precarico il value dell'input di ricerca con la stringa searchKeyword
    model.addAttribute("preloadSearch", searchKeyword);
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
    Book book = new Book();
    //book.setTitle("Default title");
    // passo tramite Model un attributo di tipo Book vuoto
    model.addAttribute("book", book);
    return "books/create";
  }

  // metodo che riceve il submit del form di creazione e salva su db il Book
  // per attivare la validazione sul formBook lo annoto come @Valid e gli errori verranno
  // inseriti nella mappa BindingResult, che deve essere il parametro immmediatamente successivo

  /*
   * Book formBook = new Book();
   * formBook.setTitle("Moby Dick")
   * */
  @PostMapping("/create")
  public String store(@Valid @ModelAttribute("book") Book formBook, BindingResult bindingResult) {
    // valido i dati del Book, cioè verifico se la mappa BindingResult ha errori
    if (bindingResult.hasErrors()) {
      // qui gestisco che ho campi non validi
      // ricaricando il template del form
      return "books/create";
    }
    // verifico se l'isbn del libro da salvare è già presente in database

    Optional<Book> bookWithIsbn = bookRepository.findByIsbn(formBook.getIsbn());
    if (bookWithIsbn.isPresent()) {
      // se esiste già ritorno un errore
      bindingResult.addError(new FieldError("book", "isbn", formBook.getIsbn(), false, null, null,
          "ISBN must be unique"));
      return "books/create";
    } else {
      // se sono validi lo salvo su db
      formBook.setCreatedAt(LocalDateTime.now());
      Book savedBook = bookRepository.save(formBook);
      // faccio una redirect alla pagina di dettaglio del libro appena creato
      return "redirect:/books/show/" + savedBook.getId();
    }
  }

  // metodo che restituisce la pagina di modifica del Book
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Integer id, Model model) {
    // recupero il libro da database
    Optional<Book> result = bookRepository.findById(id);
    // verifico se il Book è presente
    if (result.isPresent()) {
      // lo passo come attributo del Model
      model.addAttribute("book", result.get());
      // ritorno il template
      return "books/edit";
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " not found");
    }
  }

  // metodo che riceve il submit del form di edit
  @PostMapping("/edit/{id}")
  public String update(@PathVariable Integer id, @Valid @ModelAttribute("book") Book formBook,
      BindingResult bindingResult) {
    Optional<Book> result = bookRepository.findById(id);
    if (result.isPresent()) {
      Book bookToEdit = result.get();
      // valido i dati del libro
      if (bindingResult.hasErrors()) {
        // se ci sono errori di validazione
        return "books/edit";
      }

      // se sono validi salvo il libro su db
      // prima di salvare i dati su db recupero il valore del campo createdAt
      formBook.setCreatedAt(bookToEdit.getCreatedAt());
      Book savedBook = bookRepository.save(formBook);
      // faccio la redirect alla pagina di dettaglio del libro
      return "redirect:/books/show/" + id;
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " not found");
    }
  }

  // metodo che cancella un Book preso per id
  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    // verifico se il Book è presente su db
    Optional<Book> result = bookRepository.findById(id);
    if (result.isPresent()) {
      // se c'è lo cancello
      bookRepository.deleteById(id);
      // mando un messaggio di successo con la redirect
      redirectAttributes.addFlashAttribute("redirectMessage",
          "Book " + result.get().getTitle() + " deleted!");
      return "redirect:/books";
    } else {
      // se non c'è sollevo un'eccezione
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with di " + id + " not found");
    }
  }

/*  @GetMapping("/search")
  // localhost:8080/books/search?keyword=dune, query string parameter -> @RequestParam
  public String search(@RequestParam(name = "keyword") String searchKeyword, Model model) {
    // faccio una select di Book solo il cui titolo contiene searchKeyword
    List<Book> bookList = bookRepository.findByTitleContaining(searchKeyword);
    model.addAttribute("bookList", bookList);
    return "books/list";
  }*/
}
