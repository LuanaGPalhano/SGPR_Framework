package org.example.DTO;

public record NutricionistaCadastroRequest(
        String nome,
        String CrnUf,
        String email,
        String senha
) {
}