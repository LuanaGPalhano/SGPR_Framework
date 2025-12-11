package org.example.strategy;

import org.example.DTO.AvaliacaoRequest;
import org.example.model.AvaliacaoDados;

public interface AvaliacaoStrategy {
    AvaliacaoDados processarAvaliacao(AvaliacaoRequest request);
}
