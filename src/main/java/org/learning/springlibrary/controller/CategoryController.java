package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
import java.util.Optional;
import org.learning.springlibrary.model.Category;
import org.learning.springlibrary.repository.CategoryRepository;
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
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public String index(Model model) {
    // al template devo fare arrivare la lista di tutte le categorie
    model.addAttribute("categoryList", categoryRepository.findAll());
    return "categories/index";
  }

  @GetMapping("/create") // /categories/create
  public String create(Model model) {
    // preparo il template col form di creazione categoria
    model.addAttribute("formCategory", new Category());
    return "categories/form";
  }

  @PostMapping("/save")
  public String store(@Valid @ModelAttribute("formCategory") Category formCategory,
      BindingResult bindingResult) {
    // valido la Category
    if (bindingResult.hasErrors()) {
      // se ci sono errori ricarico la pagina col form
      return "categories/form";
    }
    // se non ci sono errori salvo la Category su db
    categoryRepository.save(formCategory);
    // faccio la redirect alla lista di Category
    return "redirect:/categories";
  }

  @GetMapping("/edit/{id}") // /categories/edit/3
  public String edit(@PathVariable Integer id, Model model) {
    Optional<Category> result = categoryRepository.findById(id);
    if (result.isPresent()) {
      model.addAttribute("formCategory", result.get());
      return "categories/form";
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "category with id " + id + " not found");
    }
  }
}
