package org.learning.springlibrary.controller;

import java.util.ArrayList;
import java.util.List;
import org.learning.springlibrary.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {

  // metodo index che mostra la lista di tutti i libri
  @GetMapping
  public String index(Model model) {
    // recupero la lista di libri
    List<Book> bookList = new ArrayList<>();
    // aggiungo la lista di libri agli attributi del Model
    model.addAttribute("bookList", bookList);
    return "books/list";
  }
}
