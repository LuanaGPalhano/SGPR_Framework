package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvaliacaoPsicologica implements AvaliacaoDados {
    private Integer nivelEstresse; // 1 a 10
    private Integer qualidadeSono; 
    private Integer nivelAnsiedade;
    private String humorPredominante;
    private String notasSessao;
}
