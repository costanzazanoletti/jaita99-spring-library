package org.learning.springlibrary.repository;

import java.util.Optional;
import org.learning.springlibrary.model.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryUserRepository extends JpaRepository<LibraryUser, Integer> {

  Optional<LibraryUser> findByEmail(String email);
}
