package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoricoPsicologico implements HistoricoDados {
    private String tratamentosAnteriores; 
    private String historicoFamiliarPsiquiatrico; 
    private String medicamentosPsicotropicos; 
    private String queixaPrincipal;
    private String redeApoio; 
}