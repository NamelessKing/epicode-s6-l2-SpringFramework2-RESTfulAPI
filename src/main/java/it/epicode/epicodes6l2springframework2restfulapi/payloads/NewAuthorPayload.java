package it.epicode.epicodes6l2springframework2restfulapi.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class NewAuthorPayload {
    @NotBlank(message = "Il nome e obbligatorio")
    private String nome;

    @NotBlank(message = "Il cognome e obbligatorio")
    private String cognome;

    @NotBlank(message = "L'email e obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotNull(message = "La data di nascita e obbligatoria")
    @Past(message = "La data di nascita deve essere nel passato")
    private LocalDate dataDiNascita;

    public NewAuthorPayload() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }
}
