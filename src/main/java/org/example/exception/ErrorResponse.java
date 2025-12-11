package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error; // Ex: "Not Found", "Bad Request"
    private String message; // Mensagem detalhada
    private String path; // URL onde ocorreu o erro

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }

    // Mapeia esta exceção para o status HTTP 404 Not Found automaticamente
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    // Mapeia esta exceção para o status HTTP 403 Forbidden
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class UnauthorizedOperationException extends RuntimeException {
        public UnauthorizedOperationException(String message) {
            super(message);
        }
    }
}