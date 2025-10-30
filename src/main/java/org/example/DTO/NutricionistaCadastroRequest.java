package org.example.DTO;

public record NutricionistaCadastroRequest(
        String nome,
        String crnUf,
        String email,
        String senha
) {
}