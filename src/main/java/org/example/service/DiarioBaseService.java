package org.example.service;

import java.util.List;
import org.springframework.stereotype.*;
import org.example.model.DiarioBase;
import org.example.model.Paciente;
import org.example.repository.DiarioBaseRepository;
import org.example.repository.PacienteRepository;
import org.example.DTO.DiarioBaseRequest;
import org.example.exception.ErrorResponse.ResourceNotFoundException;
import org.example.template.DiarioFactory;
import org.example.template.DiarioTemplate;
import org.example.template.DiarioTipo;

@Service
public class DiarioBaseService {
    private final DiarioBaseRepository diarioRepository;
    private final PacienteRepository pacienteRepository;
    private final DiarioFactory diarioFactory;

    public DiarioBaseService(DiarioBaseRepository diarioRepository, PacienteRepository pacienteRepository, DiarioFactory diarioFactory){
        this.diarioRepository = diarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.diarioFactory = diarioFactory;
    }

    public DiarioBase salvar(DiarioBaseRequest request, DiarioTipo tipo){
        DiarioTemplate template = diarioFactory.getTemplate(tipo);
        return template.salvarDiario(request);
    }

    public List<DiarioBase> listarTodos() {
        return diarioRepository.findAll();
    }

    public List<DiarioBase> listarPorPacienteCpf(String cpf){
        Paciente paciente = pacienteRepository.findByCpf(cpf)
        .orElseThrow(()-> new ResourceNotFoundException("Paciente com esse cpf não encontrado"));  
        return diarioRepository.findByPacienteId(paciente.getId());
    }

}