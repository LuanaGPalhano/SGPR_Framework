package org.example.template;

import org.example.DTO.DiarioBaseRequest;
import org.example.model.DiarioBase;
import org.example.exception.ErrorResponse;
import org.example.repository.DiarioBaseRepository;
import org.example.repository.PacienteRepository;
import org.springframework.stereotype.Component;

@Component("TERAPIA")
public class DiarioTerapia extends DiarioDefault{
    public DiarioTerapia(DiarioBaseRepository diarioBaseRepository, PacienteRepository pacienteRepository){
        super(diarioBaseRepository, pacienteRepository);
    }

    @Override
    protected void validar(DiarioBase diario, DiarioBaseRequest request){
            if(request.texto() == null || request.texto().isBlank()){
                throw new ErrorResponse.UnauthorizedOperationException("O diário precisa que algo seja escrito");
            }

            if(request.texto().length() < 50){
                throw new ErrorResponse.UnauthorizedOperationException("O texto está muito curto. Por favor, escreva mais.");
            }  
        
    }
}