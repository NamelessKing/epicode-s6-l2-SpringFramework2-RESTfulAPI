package it.epicode.epicodes6l2springframework2restfulapi.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class NewBlogPostPayload {
    @NotBlank(message = "La categoria e obbligatoria")
    private String categoria;

    @NotBlank(message = "Il titolo e obbligatorio")
    private String titolo;

    @NotBlank(message = "Il contenuto e obbligatorio")
    private String contenuto;

    @Positive(message = "Il tempo di lettura deve essere positivo")
    private int tempoDiLettura;

    public NewBlogPostPayload() {
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public int getTempoDiLettura() {
        return tempoDiLettura;
    }

    public void setTempoDiLettura(int tempoDiLettura) {
        this.tempoDiLettura = tempoDiLettura;
    }
}
