package it.epicode.epicodes6l2springframework2restfulapi.services;

import it.epicode.epicodes6l2springframework2restfulapi.dto.AuthorResponseDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.NewAuthorDto;
import it.epicode.epicodes6l2springframework2restfulapi.entities.Author;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.ConflictException;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.NotFoundException;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.AuthorRepository;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.BlogPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AuthorsService {
    private static final String AVATAR_BASE_URL = "https://ui-avatars.com/api/?name=";

    private final AuthorRepository authorRepository;
    private final BlogPostRepository blogPostRepository;
    private final ImageUploadService imageUploadService;

    public AuthorsService(
            AuthorRepository authorRepository,
            BlogPostRepository blogPostRepository,
            ImageUploadService imageUploadService
    ) {
        this.authorRepository = authorRepository;
        this.blogPostRepository = blogPostRepository;
        this.imageUploadService = imageUploadService;
    }

    public List<AuthorResponseDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public AuthorResponseDto save(NewAuthorDto payload) {
        String avatar = buildAvatarUrl(payload.nome(), payload.cognome());
        Author author = new Author(
                payload.nome(),
                payload.cognome(),
                payload.email(),
                payload.dataDiNascita(),
                avatar
        );
        return toDto(authorRepository.save(author));
    }

    public AuthorResponseDto findById(long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));
        return toDto(author);
    }

    @Transactional
    public AuthorResponseDto findByIdAndUpdate(long authorId, NewAuthorDto payload) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));
        author.setNome(payload.nome());
        author.setCognome(payload.cognome());
        author.setEmail(payload.email());
        author.setDataDiNascita(payload.dataDiNascita());
        return toDto(authorRepository.save(author));
    }

    @Transactional
    public void findByIdAndDelete(long authorId) {
        if (blogPostRepository.existsByAuthorId(authorId)) {
            throw new ConflictException("Impossibile eliminare l'autore: ha blog post associati");
        }
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));
        authorRepository.delete(author);
    }

    @Transactional
    public String uploadAvatar(long authorId, MultipartFile file) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Autore con id " + authorId + " non trovato"));
        String url = imageUploadService.uploadImage(file);
        author.setAvatar(url);
        authorRepository.save(author);
        return url;
    }

    private String buildAvatarUrl(String nome, String cognome) {
        String safeNome = nome.trim().replace(" ", "+");
        String safeCognome = cognome.trim().replace(" ", "+");
        return AVATAR_BASE_URL + safeNome + "+" + safeCognome;
    }

    private AuthorResponseDto toDto(Author author) {
        return new AuthorResponseDto(
                author.getId(),
                author.getNome(),
                author.getCognome(),
                author.getEmail(),
                author.getDataDiNascita(),
                author.getAvatar()
        );
    }

}
