package it.epicode.epicodes6l2springframework2restfulapi.dto;

public record BlogPostResponseDto(
        Long id,
        String categoria,
        String titolo,
        String cover,
        String contenuto,
        int tempoDiLettura,
        Long authorId
) {
}
