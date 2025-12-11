package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvaliacaoNutricionista implements AvaliacaoDados {
    private Double caloriasDiarias;
    private Double proteinaCalculada;
    private Double carboCalculado;
    private Double gorduraCalculada;
    private String objetivoNutricional; 
    private String restricoesAlimentares;
}
