package org.example.template;

import org.example.DTO.DiarioBaseRequest;
import org.example.model.DiarioBase;
import org.example.exception.ErrorResponse;
import org.example.repository.DiarioBaseRepository;
import org.example.repository.PacienteRepository;
import org.springframework.stereotype.Component;

@Component("TREINO")
public class DiarioTreino extends DiarioDefault {

    public DiarioTreino(DiarioBaseRepository diarioBaseRepository, PacienteRepository pacienteRepository) {
        super(diarioBaseRepository, pacienteRepository);
    }

    @Override
    protected void validar(DiarioBase diario, DiarioBaseRequest request) {

        if (request.texto() == null || request.texto().isBlank()) {
            throw new ErrorResponse.ValidationException("Descreva melhor o treino");
        }

        if (request.texto().length() < 20) {
            throw new ErrorResponse.ValidationException("Descreva melhor o treino");
        }
    }
}
