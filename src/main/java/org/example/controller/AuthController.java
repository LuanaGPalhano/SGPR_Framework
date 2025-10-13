package org.example.controller;

import org.example.DTO.LoginRequest;
import org.example.DTO.LoginResponse;
import org.example.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
// @CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Chama o serviço de autenticação com o DTO
        Optional<LoginResponse> response = authService.autenticar(loginRequest);

        // Retorna 200 OK com os dados se o login teve sucesso, ou 401 Unauthorized se falhou
        return response.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}