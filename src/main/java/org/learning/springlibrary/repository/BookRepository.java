package org.learning.springlibrary.repository;

import org.learning.springlibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

// interfaccia che eredita da JpaRepository tutti i metodi che permettono di fare le CRUD
// i generics chiedono: che tipo di dato è l'entità (Book) e che tipo di dato è la chiave primaria (Integer)
public interface BookRepository extends JpaRepository<Book, Integer> {

}
