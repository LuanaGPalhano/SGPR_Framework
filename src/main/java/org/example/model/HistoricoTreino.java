package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoricoTreino implements HistoricoDados {
    private String lesoesPrevias; 
    private String cirurgiasRealizadas;
    private boolean praticaEsporteAtualmente;
    private String modalidadeEsportiva;
    private String disponibilidadeTempo; 
    private boolean doresCronicas;
}
