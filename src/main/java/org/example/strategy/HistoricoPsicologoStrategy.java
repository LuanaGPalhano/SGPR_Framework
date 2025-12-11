package org.example.strategy;

import org.example.DTO.HistoricoRequest;
import org.example.model.HistoricoPsicologico;
import org.springframework.stereotype.Component;

@Component
public class HistoricoPsicologoStrategy implements HistoricoStrategy {

    @Override
    public HistoricoPsicologico processarHistorico(HistoricoRequest req) {
        return HistoricoPsicologico.builder()
                .tratamentosAnteriores(req.getString("tratamentosAnteriores"))
                .historicoFamiliarPsiquiatrico(req.getString("historicoFamiliar"))
                .medicamentosPsicotropicos(req.getString("medicamentosPsico"))
                .queixaPrincipal(req.getString("queixa"))
                .redeApoio(req.getString("redeApoio"))
                .build();
    }
}