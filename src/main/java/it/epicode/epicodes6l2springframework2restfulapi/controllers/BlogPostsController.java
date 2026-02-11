package it.epicode.epicodes6l2springframework2restfulapi.controllers;

import it.epicode.epicodes6l2springframework2restfulapi.entities.BlogPost;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.BadRequestException;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.NewBlogPostRequest;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.UpdateBlogPostRequest;
import it.epicode.epicodes6l2springframework2restfulapi.services.BlogPostsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogPosts")
public class BlogPostsController {
    private final BlogPostsService blogPostsService;

    public BlogPostsController(BlogPostsService blogPostsService) {
        this.blogPostsService = blogPostsService;
    }

    @GetMapping
    public ResponseEntity<Page<BlogPost>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        if (page < 0) {
            throw new BadRequestException("Il parametro page deve essere >= 0");
        }
        if (size < 1 || size > 100) {
            throw new BadRequestException("Il parametro size deve essere tra 1 e 100");
        }
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(blogPostsService.findAll(pageRequest));
    }

    @GetMapping("/{blogPostId}")
    public ResponseEntity<BlogPost> findById(@PathVariable long blogPostId) {
        return ResponseEntity.ok(blogPostsService.findById(blogPostId));
    }

    @PostMapping
    public ResponseEntity<BlogPost> save(@Valid @RequestBody NewBlogPostRequest payload) {
        BlogPost saved = blogPostsService.save(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{blogPostId}")
    public ResponseEntity<BlogPost> update(
            @PathVariable long blogPostId,
            @Valid @RequestBody UpdateBlogPostRequest payload
    ) {
        return ResponseEntity.ok(blogPostsService.findByIdAndUpdate(blogPostId, payload));
    }

    @DeleteMapping("/{blogPostId}")
    public ResponseEntity<Void> delete(@PathVariable long blogPostId) {
        blogPostsService.findByIdAndDelete(blogPostId);
        return ResponseEntity.noContent().build();
    }
}
