package it.epicode.epicodes6l2springframework2restfulapi.repositories;

import it.epicode.epicodes6l2springframework2restfulapi.entities.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    boolean existsByAuthorId(Long authorId);
}
