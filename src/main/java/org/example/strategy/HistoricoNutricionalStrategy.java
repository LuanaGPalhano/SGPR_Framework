package org.example.strategy;

import org.example.DTO.HistoricoRequest;
import org.example.model.HistoricoNutricional;
import org.springframework.stereotype.Component;

@Component
public class HistoricoNutricionalStrategy implements HistoricoStrategy {

    @Override
    public HistoricoNutricional processarHistorico(HistoricoRequest req) {
        return HistoricoNutricional.builder()
                .alergiasAlimentares(req.getString("alergias"))
                .medicamentosUsoContinuo(req.getString("medicamentos"))
                .suplementacaoAtual(req.getString("suplementacao"))
                .historicoFamiliarDoencas(req.getString("historicoFamiliar"))
                .funcionamentoIntestinal(req.getString("intestino"))
                .aversoesAlimentares(req.getString("aversoes"))
                .build();
    }
}
