package it.epicode.epicodes6l2springframework2restfulapi.controllers;

import it.epicode.epicodes6l2springframework2restfulapi.entities.Author;
import it.epicode.epicodes6l2springframework2restfulapi.payloads.NewAuthorPayload;
import it.epicode.epicodes6l2springframework2restfulapi.services.AuthorsService;
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
@RequestMapping("/authors")
public class AuthorsController {
    private final AuthorsService authorsService;

    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> findAll() {
        return ResponseEntity.ok(authorsService.findAll());
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<Author> findById(@PathVariable long authorId) {
        return ResponseEntity.ok(authorsService.findById(authorId));
    }

    @PostMapping
    public ResponseEntity<Author> save(@Valid @RequestBody NewAuthorPayload payload) {
        Author saved = authorsService.save(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<Author> update(
            @PathVariable long authorId,
            @Valid @RequestBody NewAuthorPayload payload
    ) {
        return ResponseEntity.ok(authorsService.findByIdAndUpdate(authorId, payload));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> delete(@PathVariable long authorId) {
        authorsService.findByIdAndDelete(authorId);
        return ResponseEntity.noContent().build();
    }
}
