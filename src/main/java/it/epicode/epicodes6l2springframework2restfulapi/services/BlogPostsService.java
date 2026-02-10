package it.epicode.epicodes6l2springframework2restfulapi.services;

import it.epicode.epicodes6l2springframework2restfulapi.entities.BlogPost;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.NotFoundException;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.NewBlogPostPayload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogPostsService {
    private static final String DEFAULT_COVER_URL = "https://picsum.photos/200/300";

    private final List<BlogPost> blogPosts = new ArrayList<>();
    private long nextId = 1;

    public List<BlogPost> findAll() {
        return new ArrayList<>(blogPosts);
    }

    public BlogPost save(NewBlogPostPayload payload) {
        long id = nextId++;
        String cover = DEFAULT_COVER_URL + "?random=" + id;
        BlogPost blogPost = new BlogPost(
                id,
                payload.getCategoria(),
                payload.getTitolo(),
                cover,
                payload.getContenuto(),
                payload.getTempoDiLettura()
        );
        blogPosts.add(blogPost);
        return blogPost;
    }

    public BlogPost findById(long blogPostId) {
        return blogPosts.stream()
                .filter(blogPost -> blogPost.getId() == blogPostId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("BlogPost con id " + blogPostId + " non trovato"));
    }

    public BlogPost findByIdAndUpdate(long blogPostId, NewBlogPostPayload payload) {
        BlogPost blogPost = findById(blogPostId);
        blogPost.setCategoria(payload.getCategoria());
        blogPost.setTitolo(payload.getTitolo());
        blogPost.setContenuto(payload.getContenuto());
        blogPost.setTempoDiLettura(payload.getTempoDiLettura());
        return blogPost;
    }

    public void findByIdAndDelete(long blogPostId) {
        BlogPost blogPost = findById(blogPostId);
        blogPosts.remove(blogPost);
    }
}
