package it.epicode.epicodes6l2springframework2restfulapi.services;

import it.epicode.epicodes6l2springframework2restfulapi.dto.BlogPostResponseDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.NewBlogPostDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.UpdateBlogPostDto;
import it.epicode.epicodes6l2springframework2restfulapi.entities.Author;
import it.epicode.epicodes6l2springframework2restfulapi.entities.BlogPost;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.NotFoundException;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.AuthorRepository;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.BlogPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class BlogPostsService {
    private static final String DEFAULT_COVER_URL = "https://picsum.photos/200/300";

    private final BlogPostRepository blogPostRepository;
    private final AuthorRepository authorRepository;
    private final ImageUploadService imageUploadService;

    public BlogPostsService(
            BlogPostRepository blogPostRepository,
            AuthorRepository authorRepository,
            ImageUploadService imageUploadService
    ) {
        this.blogPostRepository = blogPostRepository;
        this.authorRepository = authorRepository;
        this.imageUploadService = imageUploadService;
    }

    @Transactional(readOnly = true)
    public Page<BlogPostResponseDto> findAll(Pageable pageable) {
        return blogPostRepository.findAll(pageable)
                .map(this::toDto);
    }

    public BlogPostResponseDto save(NewBlogPostDto payload) {
        Author author = authorRepository.findById(payload.authorId())
                .orElseThrow(() -> new NotFoundException("Autore con id " + payload.authorId() + " non trovato"));
        String cover = DEFAULT_COVER_URL + "?random=" + UUID.randomUUID();
        BlogPost blogPost = new BlogPost(
                payload.categoria(),
                payload.title(),
                cover,
                payload.content(),
                payload.readingTime(),
                author
        );
        return toDto(blogPostRepository.save(blogPost));
    }

    @Transactional(readOnly = true)
    public BlogPostResponseDto findById(long blogPostId) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new NotFoundException("BlogPost con id " + blogPostId + " non trovato"));
        return toDto(blogPost);
    }

    @Transactional
    public BlogPostResponseDto findByIdAndUpdate(long blogPostId, UpdateBlogPostDto payload) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new NotFoundException("BlogPost con id " + blogPostId + " non trovato"));
        blogPost.setCategoria(payload.categoria());
        blogPost.setTitolo(payload.titolo());
        blogPost.setContenuto(payload.contenuto());
        blogPost.setTempoDiLettura(payload.tempoDiLettura());
        return toDto(blogPostRepository.save(blogPost));
    }

    @Transactional
    public void findByIdAndDelete(long blogPostId) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new NotFoundException("BlogPost con id " + blogPostId + " non trovato"));
        blogPostRepository.delete(blogPost);
    }

    @Transactional
    public String uploadCover(long blogPostId, MultipartFile file) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new NotFoundException("BlogPost con id " + blogPostId + " non trovato"));
        String url = imageUploadService.uploadImage(file);
        blogPost.setCover(url);
        blogPostRepository.save(blogPost);
        return url;
    }

    private BlogPostResponseDto toDto(BlogPost blogPost) {
        return new BlogPostResponseDto(
                blogPost.getId(),
                blogPost.getCategoria(),
                blogPost.getTitolo(),
                blogPost.getCover(),
                blogPost.getContenuto(),
                blogPost.getTempoDiLettura(),
                blogPost.getAuthor() != null ? blogPost.getAuthor().getId() : null
        );
    }
}
