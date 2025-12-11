package org.example.DTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.enums.ProfissionalTipo;
import org.example.model.Historico;

import java.util.Map;

public record HistoricoResponse(
    Long id,
    
    // --- Campos Fixos ---
    String estadoCivil,
    String ocupacao,
    boolean bebe,
    boolean fuma,
    
    // --- Contexto ---
    Long pacienteId,
    ProfissionalTipo tipo, 
    Object dados 
) {
    
    // Método estático Factory para converter Entity -> DTO com segurança
    public static HistoricoResponse fromEntity(Historico historico, ObjectMapper mapper) {
        
        Object dadosProcessados = null;

        try {
            if (historico.getDadosEspecificos() != null && !historico.getDadosEspecificos().isEmpty()) {
                dadosProcessados = mapper.readValue(historico.getDadosEspecificos(), Map.class);
            }
        } catch (Exception e) {
            System.err.println("Erro ao converter JSON do histórico: " + e.getMessage());
        }

        return new HistoricoResponse(
            historico.getId(),
            historico.getEstadoCivil(),
            historico.getOcupacao(),
            historico.isBebe(),
            historico.isFuma(),
            historico.getPaciente() != null ? historico.getPaciente().getId() : null,
            historico.getTipoProfissional(),
            dadosProcessados
        );
    }
}
