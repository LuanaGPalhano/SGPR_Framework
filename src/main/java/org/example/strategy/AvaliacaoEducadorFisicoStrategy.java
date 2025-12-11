package org.example.strategy;

import org.example.DTO.AvaliacaoRequest;
import org.example.model.AvaliacaoEducadorFisico;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoEducadorFisicoStrategy implements AvaliacaoStrategy {

    @Override
    public AvaliacaoEducadorFisico processarAvaliacao(AvaliacaoRequest req) {
        double peso = req.getDouble("peso");
        double altura = req.getDouble("altura");
        
        // Lógica de negócio: Cálculo do IMC
        double imc = (altura > 0) ? peso / (altura * altura) : 0.0;

        return AvaliacaoEducadorFisico.builder()
                .peso(peso)
                .altura(altura)
                .imc(imc) 
                .percentualGordura(req.getDouble("percentualGordura"))
                .circCintura(req.getDouble("circCintura"))
                .observacoesFisicas(req.getString("observacoes"))
                .build();
    }
}
