package org.example.strategy;

import org.example.DTO.HistoricoRequest;
import org.example.model.HistoricoTreino;
import org.springframework.stereotype.Component;

@Component
public class HistoricoEducadorFisicoStrategy implements HistoricoStrategy {

    @Override
    public HistoricoTreino processarHistorico(HistoricoRequest req) {
        return HistoricoTreino.builder()
                .lesoesPrevias(req.getString("lesoes"))
                .cirurgiasRealizadas(req.getString("cirurgias"))
                .praticaEsporteAtualmente(req.getBoolean("praticaEsporte"))
                .modalidadeEsportiva(req.getString("modalidade"))
                .disponibilidadeTempo(req.getString("disponibilidade"))
                .doresCronicas(req.getBoolean("doresCronicas"))
                .build();
    }
}