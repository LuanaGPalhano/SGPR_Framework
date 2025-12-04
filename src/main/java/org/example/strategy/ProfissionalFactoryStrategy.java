package org.example.strategy;

import org.example.DTO.ProfissionalCadastroRequest;
import org.example.model.Profissional;

public interface ProfissionalFactoryStrategy {

    // Método que verifica se essa estratégia serve para o tipo solicitado
    boolean supports(String tipoProfissional);

    // Método que cria a instância correta
    Profissional criarProfissional(ProfissionalCadastroRequest dados);
}