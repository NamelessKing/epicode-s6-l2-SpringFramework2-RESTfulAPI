package it.epicode.epicodes6l2springframework2restfulapi.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
