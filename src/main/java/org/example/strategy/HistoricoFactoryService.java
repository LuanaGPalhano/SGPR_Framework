package org.example.strategy;

import org.example.enums.ProfissionalTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoFactoryService {

    private final HistoricoNutricionalStrategy nutricionalStrategy;
    private final HistoricoEducadorFisicoStrategy educadorFisicoStrategy;
    private final HistoricoPsicologoStrategy psicologoStrategy;

    public HistoricoStrategy getStrategy(ProfissionalTipo tipo) {
        return switch (tipo) {
            case NUTRICIONISTA -> nutricionalStrategy;
            case EDUCACAO_FISICA -> educadorFisicoStrategy;
            case PSICOLOGO -> psicologoStrategy;
            default -> throw new IllegalArgumentException("Tipo de profissional inválido para histórico: " + tipo);
        };
    }
}
