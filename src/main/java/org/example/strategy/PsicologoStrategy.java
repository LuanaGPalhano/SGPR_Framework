package org.example.strategy;

import org.example.DTO.ProfissionalCadastroRequest;
import org.example.model.Profissional;
import org.example.model.Psicologo;
import org.springframework.stereotype.Component;

@Component
public class PsicologoStrategy implements ProfissionalFactoryStrategy {

    @Override
    public boolean supports(String tipoProfissional) {
        return "PSICOLOGO".equalsIgnoreCase(tipoProfissional);
    }

    @Override
    public Profissional criarProfissional(ProfissionalCadastroRequest dados) {
        Psicologo psicologo = new Psicologo();
        psicologo.setCrp(dados.registroProfissional());
        return psicologo;
    }
}