package org.example.strategy;

import org.example.enums.ProfissionalTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliacaoFactoryService {

    private final AvaliacaoEducadorFisicoStrategy educadorFisicoStrategy;
    private final AvaliacaoNutricionistaStrategy nutricionistaStrategy;
    private final AvaliacaoPsicologoStrategy psicologoStrategy;

    public AvaliacaoStrategy getStrategy(ProfissionalTipo tipo) {
        return switch (tipo) {
            case EDUCACAO_FISICA -> educadorFisicoStrategy;
            case NUTRICIONISTA -> nutricionistaStrategy;
            case PSICOLOGO -> psicologoStrategy;
            default -> throw new IllegalArgumentException("Tipo de profissional não suportado para avaliação");
        };
    }
}
