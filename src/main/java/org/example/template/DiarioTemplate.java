package org.example.template;

import org.example.DTO.DiarioBaseRequest;
import org.example.model.DiarioBase;
import org.example.model.EntradaDiario;
import org.example.model.Paciente;
import org.example.template.DiarioTipo;

public abstract class DiarioTemplate {
    public final DiarioBase salvarDiario(DiarioBaseRequest request){
        Paciente paciente = buscarPaciente(request);
        DiarioBase diario = criarDiario(request, paciente);
        validar(diario, request);
        adicionarEntradas(diario, request);
        return persistir(diario);
    }

    protected abstract Paciente buscarPaciente(DiarioBaseRequest request);
    protected abstract DiarioBase persistir(DiarioBase diario);

    protected DiarioBase criarDiario(DiarioBaseRequest request, Paciente paciente){
        return DiarioBase.builder()
        .texto(request.texto())
        .imgURL(request.imgURL())
        .paciente(paciente)
        .build();
    }

    protected void validar(DiarioBase diario, DiarioBaseRequest request){}

    protected void adicionarEntradas(DiarioBase diario, DiarioBaseRequest request){
        if(request.entradas() != null && !request.entradas().isEmpty()){
            var entradas = request.entradas().stream()
                .map(e -> new EntradaDiario(null, e.descricao(), diario))
                .toList();

            diario.setEntradasDiario(entradas);
        }
    }
}
