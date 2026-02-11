package it.epicode.epicodes6l2springframework2restfulapi.services;

import it.epicode.epicodes6l2springframework2restfulapi.entities.Author;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.ConflictException;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.NotFoundException;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.NewAuthorPayload;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.AuthorRepository;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.BlogPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorsService {
    private static final String AVATAR_BASE_URL = "https://ui-avatars.com/api/?name=";

    private final AuthorRepository authorRepository;
    private final BlogPostRepository blogPostRepository;

    public AuthorsService(AuthorRepository authorRepository, BlogPostRepository blogPostRepository) {
        this.authorRepository = authorRepository;
        this.blogPostRepository = blogPostRepository;
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author save(NewAuthorPayload payload) {
        String avatar = buildAvatarUrl(payload.getNome(), payload.getCognome());
        Author author = new Author(
                payload.getNome(),
                payload.getCognome(),
                payload.getEmail(),
                payload.getDataDiNascita(),
                avatar
        );
        return authorRepository.save(author);
    }

    public Author findById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));
    }

    @Transactional
    public Author findByIdAndUpdate(long authorId, NewAuthorPayload payload) {
        Author author = findById(authorId);
        author.setNome(payload.getNome());
        author.setCognome(payload.getCognome());
        author.setEmail(payload.getEmail());
        author.setDataDiNascita(payload.getDataDiNascita());
        author.setAvatar(buildAvatarUrl(payload.getNome(), payload.getCognome()));
        return authorRepository.save(author);
    }

    @Transactional
    public void findByIdAndDelete(long authorId) {
        if (blogPostRepository.existsByAuthorId(authorId)) {
            throw new ConflictException("Impossibile eliminare l'autore: ha blog post associati");
        }
        Author author = findById(authorId);
        authorRepository.delete(author);
    }

    private String buildAvatarUrl(String nome, String cognome) {
        String safeNome = nome.trim().replace(" ", "+");
        String safeCognome = cognome.trim().replace(" ", "+");
        return AVATAR_BASE_URL + safeNome + "+" + safeCognome;
    }
}
