package org.learning.springlibrary.repository;

import org.learning.springlibrary.model.BookType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookTypeRepository extends JpaRepository<BookType, Integer> {

}
