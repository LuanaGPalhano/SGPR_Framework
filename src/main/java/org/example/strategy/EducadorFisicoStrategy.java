package org.example.strategy;

import org.example.DTO.ProfissionalCadastroRequest;
import org.example.model.EducadorFisico;
import org.example.model.Profissional;
import org.springframework.stereotype.Component;

@Component
public class EducadorFisicoStrategy implements ProfissionalFactoryStrategy {

    @Override
    public boolean supports(String tipoProfissional) {
        return "EDUCADOR_FISICO".equalsIgnoreCase(tipoProfissional);
    }

    @Override
    public Profissional criarProfissional(ProfissionalCadastroRequest dados) {
        EducadorFisico educador = new EducadorFisico();
        educador.setCref(dados.registroProfissional());
        return educador;
    }
}