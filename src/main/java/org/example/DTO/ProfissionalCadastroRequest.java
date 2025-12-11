package org.example.DTO;

public record ProfissionalCadastroRequest(
        String nome,
        String email,
        String senha,
        String registroProfissional, // Será o CRN, CREF ou CRP dependendo do tipo
        String tipoProfissional // "NUTRICIONISTA", "EDUCADOR_FISICO", "PSICOLOGO"
) {
}