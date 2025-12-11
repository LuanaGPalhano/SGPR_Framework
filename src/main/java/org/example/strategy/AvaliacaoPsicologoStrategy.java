package org.example.strategy;

import org.example.DTO.AvaliacaoRequest;
import org.example.model.AvaliacaoPsicologica;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoPsicologoStrategy implements AvaliacaoStrategy {

    @Override
    public AvaliacaoPsicologica processarAvaliacao(AvaliacaoRequest req) {
        return AvaliacaoPsicologica.builder()
                .nivelEstresse(req.getInt("nivelEstresse"))
                .qualidadeSono(req.getInt("qualidadeSono"))
                .nivelAnsiedade(req.getInt("ansiedade"))
                .humorPredominante(req.getString("humor"))
                .notasSessao(req.getString("notas"))
                .build();
    }
}
