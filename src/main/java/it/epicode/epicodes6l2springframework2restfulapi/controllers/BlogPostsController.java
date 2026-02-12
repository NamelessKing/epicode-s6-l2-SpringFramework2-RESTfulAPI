package it.epicode.epicodes6l2springframework2restfulapi.controllers;

import it.epicode.epicodes6l2springframework2restfulapi.dto.BlogPostResponseDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.ImageUploadResponseDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.NewBlogPostDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.UpdateBlogPostDto;
import it.epicode.epicodes6l2springframework2restfulapi.exceptions.BadRequestException;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/blogPosts")
public class BlogPostsController {
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String DEFAULT_SORT_DIR = "asc";
    private final BlogPostsService blogPostsService;

    public BlogPostsController(BlogPostsService blogPostsService) {
        this.blogPostsService = blogPostsService;
    }

    @GetMapping
    public ResponseEntity<Page<BlogPostResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        if (page < 0) {
            throw new BadRequestException("Il parametro page deve essere >= 0");
        }
        if (size < 1 || size > 100) {
            throw new BadRequestException("Il parametro size deve essere tra 1 e 100");
        }
        String safeSortBy = resolveSortField(sortBy);
        String safeSortDir = resolveSortDir(sortDir);
        Sort sort = safeSortDir.equalsIgnoreCase("desc")
                ? Sort.by(safeSortBy).descending()
                : Sort.by(safeSortBy).ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(blogPostsService.findAll(pageRequest));
    }

    @GetMapping("/{blogPostId}")
    public ResponseEntity<BlogPostResponseDto> findById(@PathVariable long blogPostId) {
        return ResponseEntity.ok(blogPostsService.findById(blogPostId));
    }

    @PostMapping
    public ResponseEntity<BlogPostResponseDto> save(@Valid @RequestBody NewBlogPostDto payload) {
        BlogPostResponseDto saved = blogPostsService.save(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{blogPostId}")
    public ResponseEntity<BlogPostResponseDto> update(
            @PathVariable long blogPostId,
            @Valid @RequestBody UpdateBlogPostDto payload
    ) {
        return ResponseEntity.ok(blogPostsService.findByIdAndUpdate(blogPostId, payload));
    }

    @DeleteMapping("/{blogPostId}")
    public ResponseEntity<Void> delete(@PathVariable long blogPostId) {
        blogPostsService.findByIdAndDelete(blogPostId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{blogPostId}/cover")
    public ResponseEntity<ImageUploadResponseDto> uploadCover(
            @PathVariable long blogPostId,
            @RequestParam("cover") MultipartFile file
    ) {
        String url = blogPostsService.uploadCover(blogPostId, file);
        return ResponseEntity.ok(new ImageUploadResponseDto(url));
    }

    private String resolveSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return DEFAULT_SORT_FIELD;
        }
        return switch (sortBy) {
            case "id", "categoria", "titolo", "tempoDiLettura" -> sortBy;
            default -> throw new BadRequestException("sortBy non valido");
        };
    }

    private String resolveSortDir(String sortDir) {
        if (sortDir == null || sortDir.isBlank()) {
            return DEFAULT_SORT_DIR;
        }
        return switch (sortDir.toLowerCase()) {
            case "asc", "desc" -> sortDir.toLowerCase();
            default -> throw new BadRequestException("sortDir non valido");
        };
    }
}
