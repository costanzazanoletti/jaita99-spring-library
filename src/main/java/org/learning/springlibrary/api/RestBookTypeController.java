package org.learning.springlibrary.api;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.learning.springlibrary.model.BookType;
import org.learning.springlibrary.repository.BookTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/booktypes")
public class RestBookTypeController {

  @Autowired
  private BookTypeRepository bookTypeRepository;

  @GetMapping // metodo per restituire la lista di tutti i BookType
  public List<BookType> list() {
    // recupero la lista di BookType da database
    List<BookType> bookTypeList = bookTypeRepository.findAll();
    return bookTypeList;
  }

  // metodo per restituire i dettagli del singolo BookType
  @GetMapping("/{id}")
  public BookType details(@PathVariable Integer id) {
    // recupero il BookType da database
    Optional<BookType> result = bookTypeRepository.findById(id);
    // se è presente lo restituisco
    if (result.isPresent()) {
      return result.get();
    } else {
      // altimenti ho un errore con HTTP 404
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  // metodo per creare un nuovo BookType
  @PostMapping
  public BookType create(@Valid @RequestBody BookType bookType) {
    // salvo il BookType su db
    return bookTypeRepository.save(bookType);
  }

  // metodo per modificare un BookType esistente
  @PutMapping("/{id}")
  public BookType update(@PathVariable Integer id, @RequestBody BookType bookType) {
    // assegno al BookType da modificare l'id preso dal path variable
    bookType.setId(id);

    // salvo le modifiche su db
    return bookTypeRepository.save(bookType);
  }

  // metodo per eliminare un BookType
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id) {
    Optional<BookType> result = bookTypeRepository.findById(id);
    if (result.isPresent()) {
      bookTypeRepository.delete(result.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
}
