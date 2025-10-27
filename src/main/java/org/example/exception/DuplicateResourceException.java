package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Mapeia esta exceção para o status HTTP 409 Conflict
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}