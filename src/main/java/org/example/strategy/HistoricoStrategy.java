package org.example.strategy;

import org.example.DTO.HistoricoRequest;
import org.example.model.HistoricoDados;

public interface HistoricoStrategy {
    HistoricoDados processarHistorico(HistoricoRequest request);
}