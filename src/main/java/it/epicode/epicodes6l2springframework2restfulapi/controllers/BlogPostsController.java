package it.epicode.epicodes6l2springframework2restfulapi.controllers;

import it.epicode.epicodes6l2springframework2restfulapi.entities.BlogPost;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.NewBlogPostPayload;
import it.epicode.epicodes6l2springframework2restfulapi.services.BlogPostsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blogPosts")
public class BlogPostsController {
    private final BlogPostsService blogPostsService;

    public BlogPostsController(BlogPostsService blogPostsService) {
        this.blogPostsService = blogPostsService;
    }

    @GetMapping
    public ResponseEntity<List<BlogPost>> findAll() {
        return ResponseEntity.ok(blogPostsService.findAll());
    }

    @GetMapping("/{blogPostId}")
    public ResponseEntity<BlogPost> findById(@PathVariable long blogPostId) {
        return ResponseEntity.ok(blogPostsService.findById(blogPostId));
    }

    @PostMapping
    public ResponseEntity<BlogPost> save(@Valid @RequestBody NewBlogPostPayload payload) {
        BlogPost saved = blogPostsService.save(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{blogPostId}")
    public ResponseEntity<BlogPost> update(
            @PathVariable long blogPostId,
            @Valid @RequestBody NewBlogPostPayload payload
    ) {
        return ResponseEntity.ok(blogPostsService.findByIdAndUpdate(blogPostId, payload));
    }

    @DeleteMapping("/{blogPostId}")
    public ResponseEntity<Void> delete(@PathVariable long blogPostId) {
        blogPostsService.findByIdAndDelete(blogPostId);
        return ResponseEntity.noContent().build();
    }
}
