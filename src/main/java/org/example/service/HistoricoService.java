package org.example.service;

import org.example.DTO.HistoricoRequest;
import org.example.DTO.HistoricoResponse;
import org.example.model.Historico;
import org.example.model.Paciente;
import org.example.repository.HistoricoRepository;
import org.springframework.stereotype.Service;
import org.example.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final PacienteRepository pacienteRepository;

    @Autowired
    public HistoricoService(HistoricoRepository historicoRepository, PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
        this.historicoRepository = historicoRepository;
    }

    public HistoricoResponse criarHistorico(Long pacienteId, HistoricoRequest request) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID: " + pacienteId));

        Historico historico = new Historico();
        historico.setPaciente(paciente);
        
        historico.setEstadoCivil(request.estadoCivil());
        historico.setOcupacao(request.ocupacao());
        historico.setAlergias(request.alergias());
        historico.setMedicamentos(request.medicamentos());
        historico.setSuplementacao(request.suplementacao());
        historico.setHistoricoFamiliar(request.historicoFamiliar());
        historico.setOutrasCondicoes(request.outrasCondicoes());
        historico.setBebe(request.isBebe());
        historico.setFuma(request.isFuma());
       
        Historico historicoSalvo = historicoRepository.save(historico);

        return new HistoricoResponse(historicoSalvo);
    }

    public HistoricoResponse buscarPorPacienteId(Long pacienteId) {
        Historico historico = historicoRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Histórico não encontrado para o paciente com ID: " + pacienteId));
        return new HistoricoResponse(historico);
    }

    public HistoricoResponse atualizarHistorico(Long historicoId, HistoricoRequest request) {
        Historico historico = historicoRepository.findById(historicoId)
                .orElseThrow(() -> new EntityNotFoundException("Histórico não encontrado com o ID: " + historicoId));

        // Atualiza os campos com os dados do DTO
        historico.setEstadoCivil(request.estadoCivil());
        historico.setOcupacao(request.ocupacao());
        historico.setAlergias(request.alergias());
        historico.setMedicamentos(request.medicamentos());
        historico.setSuplementacao(request.suplementacao());
        historico.setHistoricoFamiliar(request.historicoFamiliar());
        historico.setOutrasCondicoes(request.outrasCondicoes());
        historico.setBebe(request.isBebe());
        historico.setFuma(request.isFuma());

        Historico historicoAtualizado = historicoRepository.save(historico);
        return new HistoricoResponse(historicoAtualizado);
    }
    
    public void deletarHistorico(Long historicoId) {
        if (!historicoRepository.existsById(historicoId)) {
            throw new RuntimeException("Histórico não encontrado com ID: " + historicoId);
        }
        historicoRepository.deleteById(historicoId);
    }
}


