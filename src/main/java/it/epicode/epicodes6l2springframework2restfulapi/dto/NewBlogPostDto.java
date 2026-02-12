package it.epicode.epicodes6l2springframework2restfulapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewBlogPostDto(
        @NotBlank(message = "La categoria e obbligatoria") String categoria,
        @NotBlank(message = "Il titolo e obbligatorio") String title,
        @NotBlank(message = "Il contenuto e obbligatorio") String content,
        @Positive(message = "Il tempo di lettura deve essere positivo") int readingTime,
        @NotNull(message = "L'autore e obbligatorio")
        @Positive(message = "L'id autore deve essere positivo")
        Long authorId
) {
}
