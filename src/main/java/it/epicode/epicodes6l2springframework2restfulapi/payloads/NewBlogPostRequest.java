package it.epicode.epicodes6l2springframework2restfulapi.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class NewBlogPostRequest {
    @NotBlank(message = "Il titolo e obbligatorio")
    private String title;

    @NotBlank(message = "Il contenuto e obbligatorio")
    private String content;

    @Positive(message = "Il tempo di lettura deve essere positivo")
    private int readingTime;

    @NotNull(message = "L'autore e obbligatorio")
    @Positive(message = "L'id autore deve essere positivo")
    private Long authorId;

    public NewBlogPostRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(int readingTime) {
        this.readingTime = readingTime;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
