package org.example.template;

import org.example.DTO.DiarioBaseRequest;
import org.example.model.DiarioBase;
import org.example.exception.ErrorResponse;
import org.example.repository.DiarioBaseRepository;
import org.example.repository.PacienteRepository;
import org.springframework.stereotype.Component;

@Component("ALIMENTAR")
public class DiarioAlimentar extends DiarioDefault{
    public DiarioAlimentar(DiarioBaseRepository diarioBaseRepository, PacienteRepository pacienteRepository){
        super(diarioBaseRepository, pacienteRepository);
    }

    @Override
    protected void validar(DiarioBase diario, DiarioBaseRequest request){
            if(request.imgURL() == null || request.imgURL().isBlank()){
                throw new ErrorResponse.ValidationException("O diário precisa de uma foto da refeição");
            }
    }
}
