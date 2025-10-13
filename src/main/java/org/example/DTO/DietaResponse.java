package org.example.DTO;

import org.example.model.Dieta;
import java.time.LocalDate;

public record DietaResponse(Long id, LocalDate dataInicio, LocalDate dataFim, String objetivo) { 
    public DietaResponse(Dieta dieta) {
        this(dieta.getId(), dieta.getDataInicio(), dieta.getDataFim(), dieta.getObjetivo());
    }
}
