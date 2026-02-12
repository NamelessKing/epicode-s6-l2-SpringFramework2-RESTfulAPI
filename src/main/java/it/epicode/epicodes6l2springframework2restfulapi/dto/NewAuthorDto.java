package it.epicode.epicodes6l2springframework2restfulapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record NewAuthorDto(
        @NotBlank(message = "Il nome e obbligatorio") String nome,
        @NotBlank(message = "Il cognome e obbligatorio") String cognome,
        @NotBlank(message = "L'email e obbligatoria") @Email(message = "Formato email non valido") String email,
        @NotNull(message = "La data di nascita e obbligatoria")
        @Past(message = "La data di nascita deve essere nel passato")
        LocalDate dataDiNascita
) {
}
