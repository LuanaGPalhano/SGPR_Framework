package org.example.strategy;

import org.example.DTO.ProfissionalCadastroRequest;
import org.example.model.Nutricionista;
import org.example.model.Profissional;
import org.springframework.stereotype.Component;

@Component
public class NutricionistaStrategy implements ProfissionalFactoryStrategy {

    @Override
    public boolean supports(String tipoProfissional) {
        return "NUTRICIONISTA".equalsIgnoreCase(tipoProfissional);
    }

    @Override
    public Profissional criarProfissional(ProfissionalCadastroRequest dados) {
        Nutricionista nutricionista = new Nutricionista();
        // Preenche APENAS o específico aqui
        nutricionista.setCrn(dados.registroProfissional());
        return nutricionista;
    }
}