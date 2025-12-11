package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoricoNutricional implements HistoricoDados {
    private String alergiasAlimentares;
    private String medicamentosUsoContinuo;
    private String suplementacaoAtual;
    private String historicoFamiliarDoencas;
    private String funcionamentoIntestinal;
    private String aversoesAlimentares; 
}
