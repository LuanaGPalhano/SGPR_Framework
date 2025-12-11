package org.example.strategy;

import org.example.DTO.AvaliacaoRequest;
import org.example.model.AvaliacaoNutricionista;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoNutricionistaStrategy implements AvaliacaoStrategy {

    @Override
    public AvaliacaoNutricionista processarAvaliacao(AvaliacaoRequest req) {
        // Exemplo de lógica: Calcular distribuição básica se não fornecida
        double calorias = req.getDouble("caloriasDiarias");
        
        return AvaliacaoNutricionista.builder()
                .caloriasDiarias(calorias)
                .proteinaCalculada(req.getDouble("proteinas"))
                .carboCalculado(req.getDouble("carboidratos"))
                .gorduraCalculada(req.getDouble("gorduras"))
                .objetivoNutricional(req.getString("objetivo"))
                .restricoesAlimentares(req.getString("restricoes"))
                .build();
    }
}
