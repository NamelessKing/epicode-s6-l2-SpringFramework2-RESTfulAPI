package it.epicode.epicodes6l2springframework2restfulapi.dto;

import java.time.LocalDate;

public record AuthorResponseDto(
        Long id,
        String nome,
        String cognome,
        String email,
        LocalDate dataDiNascita,
        String avatar
) {
}
