package org.example.DTO;

import org.example.model.Profissional;

public record ProfissionalResponse(Long id, String nome, String registro, String email, String tipo) {
    public ProfissionalResponse(Profissional profissional) {
        this(profissional.getId(),
                profissional.getNome(),
                profissional.getRegistroProfissional(),
                profissional.getEmail(),
                profissional.getClass().getSimpleName().toUpperCase()); // Retorna o tipo da classe
    }
}