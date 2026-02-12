package it.epicode.epicodes6l2springframework2restfulapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateBlogPostDto(
        @NotBlank(message = "La categoria e obbligatoria") String categoria,
        @NotBlank(message = "Il titolo e obbligatorio") String titolo,
        @NotBlank(message = "Il contenuto e obbligatorio") String contenuto,
        @Positive(message = "Il tempo di lettura deve essere positivo") int tempoDiLettura
) {
}
