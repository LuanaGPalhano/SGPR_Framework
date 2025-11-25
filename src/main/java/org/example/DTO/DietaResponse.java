package org.example.DTO;

import java.time.LocalDate;
import java.util.List;

// Este record agora aceita os 7 argumentos
// que o seu DietaService está tentando passar
public record DietaResponse(
        Long id,
        LocalDate dataInicio,
        LocalDate dataFim,
        String objetivo,
        Long nutricionistaId, // Campo para o ID do nutricionista
        Long pacienteId,      // Campo para o ID do paciente
        List<RefeicaoResponse> refeicoes // A lista de refeições (que já corrigimos)
) {
}