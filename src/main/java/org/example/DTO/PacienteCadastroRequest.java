package org.example.DTO;

public record PacienteCadastroRequest(
        String nome,
        String cpf,
        String email,
        String senha
) {
}
