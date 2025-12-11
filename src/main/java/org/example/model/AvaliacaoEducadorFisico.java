package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvaliacaoEducadorFisico implements AvaliacaoDados {
    private Double peso;
    private Double altura;
    private Double imc;
    private Double percentualGordura;
    private Double circCintura;
    private String observacoesFisicas;
}
