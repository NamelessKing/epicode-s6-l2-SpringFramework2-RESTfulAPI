package it.epicode.epicodes6l2springframework2restfulapi.services;

import it.epicode.epicodes6l2springframework2restfulapi.entities.Author;
import it.epicode.epicodes6l2springframework2restfulapi.entities.BlogPost;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.NotFoundException;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.NewBlogPostRequest;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.UpdateBlogPostRequest;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.AuthorRepository;
import it.epicode.epicodes6l2springframework2restfulapi.repositories.BlogPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BlogPostsService {
    private static final String DEFAULT_COVER_URL = "https://picsum.photos/200/300";
    private static final String DEFAULT_CATEGORY = "General";

    private final BlogPostRepository blogPostRepository;
    private final AuthorRepository authorRepository;

    public BlogPostsService(BlogPostRepository blogPostRepository, AuthorRepository authorRepository) {
        this.blogPostRepository = blogPostRepository;
        this.authorRepository = authorRepository;
    }

    public Page<BlogPost> findAll(Pageable pageable) {
        return blogPostRepository.findAll(pageable);
    }

    public BlogPost save(NewBlogPostRequest payload) {
        Author author = authorRepository.findById(payload.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Autore con id " + payload.getAuthorId() + " non trovato"));
        String cover = DEFAULT_COVER_URL + "?random=" + UUID.randomUUID();
        BlogPost blogPost = new BlogPost(
                DEFAULT_CATEGORY,
                payload.getTitle(),
                cover,
                payload.getContent(),
                payload.getReadingTime(),
                author
        );
        return blogPostRepository.save(blogPost);
    }

    public BlogPost findById(long blogPostId) {
        return blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new NotFoundException("BlogPost con id " + blogPostId + " non trovato"));
    }

    @Transactional
    public BlogPost findByIdAndUpdate(long blogPostId, UpdateBlogPostRequest payload) {
        BlogPost blogPost = findById(blogPostId);
        blogPost.setCategoria(payload.getCategoria());
        blogPost.setTitolo(payload.getTitolo());
        blogPost.setContenuto(payload.getContenuto());
        blogPost.setTempoDiLettura(payload.getTempoDiLettura());
        return blogPostRepository.save(blogPost);
    }

    @Transactional
    public void findByIdAndDelete(long blogPostId) {
        BlogPost blogPost = findById(blogPostId);
        blogPostRepository.delete(blogPost);
    }
}
