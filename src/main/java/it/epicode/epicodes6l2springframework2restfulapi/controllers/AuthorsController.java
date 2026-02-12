package it.epicode.epicodes6l2springframework2restfulapi.controllers;

import it.epicode.epicodes6l2springframework2restfulapi.dto.AuthorResponseDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.ImageUploadResponseDto;
import it.epicode.epicodes6l2springframework2restfulapi.dto.NewAuthorDto;
import it.epicode.epicodes6l2springframework2restfulapi.services.AuthorsService;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorsController {
    private final AuthorsService authorsService;

    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> findAll() {
        return ResponseEntity.ok(authorsService.findAll());
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> findById(@PathVariable long authorId) {
        return ResponseEntity.ok(authorsService.findById(authorId));
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> save(@Valid @RequestBody NewAuthorDto payload) {
        AuthorResponseDto saved = authorsService.save(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> update(
            @PathVariable long authorId,
            @Valid @RequestBody NewAuthorDto payload
    ) {
        return ResponseEntity.ok(authorsService.findByIdAndUpdate(authorId, payload));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> delete(@PathVariable long authorId) {
        authorsService.findByIdAndDelete(authorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{authorId}/avatar")
    public ResponseEntity<ImageUploadResponseDto> uploadAvatar(
            @PathVariable long authorId,
            @RequestParam("avatar") MultipartFile file
    ) {
        String url = authorsService.uploadAvatar(authorId, file);
        return ResponseEntity.ok(new ImageUploadResponseDto(url));
    }
}
