package org.example.DTO;

import org.example.model.Nutricionista;

public record NutricionistaResponse(Long id, String nome, String cpf, String email) {

    public NutricionistaResponse(Nutricionista nutricionista) {
        this(nutricionista.getId(), nutricionista.getNome(), nutricionista.getCrnUf(), nutricionista.getEmail());
    }
}