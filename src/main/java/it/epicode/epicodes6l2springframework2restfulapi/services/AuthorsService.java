package it.epicode.epicodes6l2springframework2restfulapi.services;

import it.epicode.epicodes6l2springframework2restfulapi.entities.Author;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.NotFoundException;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.NewAuthorPayload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorsService {
    private static final String AVATAR_BASE_URL = "https://ui-avatars.com/api/?name=";

    private final List<Author> authors = new ArrayList<>();
    private long nextId = 1;

    public List<Author> findAll() {
        return new ArrayList<>(authors);
    }

    public Author save(NewAuthorPayload payload) {
        long id = nextId++;
        String avatar = buildAvatarUrl(payload.getNome(), payload.getCognome());
        Author author = new Author(
                id,
                payload.getNome(),
                payload.getCognome(),
                payload.getEmail(),
                payload.getDataDiNascita(),
                avatar
        );
        authors.add(author);
        return author;
    }

    public Author findById(long authorId) {
        return authors.stream()
                .filter(author -> author.getId() == authorId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));
    }

    public Author findByIdAndUpdate(long authorId, NewAuthorPayload payload) {
        Author author = findById(authorId);
        author.setNome(payload.getNome());
        author.setCognome(payload.getCognome());
        author.setEmail(payload.getEmail());
        author.setDataDiNascita(payload.getDataDiNascita());
        author.setAvatar(buildAvatarUrl(payload.getNome(), payload.getCognome()));
        return author;
    }

    public void findByIdAndDelete(long authorId) {
        Author author = findById(authorId);
        authors.remove(author);
    }

    private String buildAvatarUrl(String nome, String cognome) {
        String safeNome = nome.trim().replace(" ", "+");
        String safeCognome = cognome.trim().replace(" ", "+");
        return AVATAR_BASE_URL + safeNome + "+" + safeCognome;
    }
}
