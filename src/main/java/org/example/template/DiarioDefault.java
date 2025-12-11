package org.example.template;

import org.springframework.stereotype.*;
import org.example.model.DiarioBase;
import org.example.model.Paciente;
import org.example.repository.DiarioBaseRepository;
import org.example.repository.PacienteRepository;
import org.example.DTO.DiarioBaseRequest;
import org.example.exception.ErrorResponse.ResourceNotFoundException;

@Component("DEFAULT")
public class DiarioDefault extends DiarioTemplate{
    private final DiarioBaseRepository diarioRepository;
    private final PacienteRepository pacienteRepository;

    public DiarioDefault(DiarioBaseRepository diarioRepository, PacienteRepository pacienteRepository){
        this.diarioRepository = diarioRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    protected Paciente buscarPaciente(DiarioBaseRequest request){
        return pacienteRepository.findByCpf(request.pacienteCpf())
        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado" + request.pacienteCpf()));
    }

    @Override
    protected DiarioBase persistir(DiarioBase diario){
        return diarioRepository.save(diario);
    }

}