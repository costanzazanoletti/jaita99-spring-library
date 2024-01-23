package org.learning.springlibrary.api;

import java.util.List;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/books")
public class RestBookController {

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public List<Book> list() {
    List<Book> books = bookRepository.findAll();
    return books;
  }
}
