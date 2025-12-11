package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handler para exceção customizada ResourceNotFoundException
    @ExceptionHandler(ErrorResponse.ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ErrorResponse.ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).substring(4) // Extrai a URI da descrição
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handler para exceção customizada UnauthorizedOperationException
    @ExceptionHandler(ErrorResponse.UnauthorizedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedOperationException(
            ErrorResponse.UnauthorizedOperationException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(),
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Handler para NoSuchElementException
    // Pode ser mapeado para 404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(
            NoSuchElementException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(), // Mensagem que você passou para orElseThrow
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handler para SecurityException (lançada no método excluirFavorito)
    // Mapeado para 403 Forbidden
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(
            SecurityException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(), // Mensagem da exceção
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Handler genérico para qualquer outra exceção não tratada especificamente
    // Mapeado para 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor.", // Mensagem genérica para o usuário
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(), // Status 409
                "Conflict",
                ex.getMessage(), // Mensagem: "CPF já cadastrado!" ou "CRN já cadastrado!"
                request.getDescription(false).substring(4)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}