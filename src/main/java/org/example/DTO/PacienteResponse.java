package org.example.DTO;

import org.example.model.Paciente;

public record PacienteResponse(Long id, String nome, String cpf, String email) {

    public PacienteResponse(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getCpf(), paciente.getEmail());
    }
}