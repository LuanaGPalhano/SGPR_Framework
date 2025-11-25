package org.example.DTO;

import java.time.LocalDate;
import java.util.List;

public record DietaCadastroRequest(
        LocalDate dataInicio,
        LocalDate dataFim,
        String objetivo,
        Long nutricionistaId,
        Long pacienteId,
        List<RefeicaoRequest> entradaDieta
) {}