package it.epicode.epicodes6l2springframework2restfulapi.repositories;

import it.epicode.epicodes6l2springframework2restfulapi.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
