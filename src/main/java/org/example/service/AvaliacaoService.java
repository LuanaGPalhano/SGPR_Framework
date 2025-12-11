package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.DTO.AvaliacaoRequest;
import org.example.DTO.AvaliacaoResponse;
import org.example.enums.ProfissionalTipo; 
import org.example.model.Avaliacao;
import org.example.model.AvaliacaoDados;
import org.example.model.Paciente;
import org.example.repository.AvaliacaoRepository;
import org.example.repository.PacienteRepository;
import org.example.strategy.AvaliacaoFactoryService;
import org.example.strategy.AvaliacaoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PacienteRepository pacienteRepository;
    
    private final AvaliacaoFactoryService avaliacaoFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, 
                            PacienteRepository pacienteRepository,
                            AvaliacaoFactoryService avaliacaoFactory,
                            ObjectMapper objectMapper) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.pacienteRepository = pacienteRepository;
        this.avaliacaoFactory = avaliacaoFactory;
        this.objectMapper = objectMapper;
    }
    public AvaliacaoResponse criarAvaliacao(Long pacienteId, AvaliacaoRequest request, ProfissionalTipo tipo) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID: " + pacienteId));

        AvaliacaoStrategy strategy = avaliacaoFactory.getStrategy(tipo);

        AvaliacaoDados dadosProcessados = strategy.processarAvaliacao(request);


        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setPaciente(paciente);
        novaAvaliacao.setDataAvaliacao(LocalDate.now());
        novaAvaliacao.setTipoProfissional(tipo);

        try {
            String jsonDados = objectMapper.writeValueAsString(dadosProcessados);
            novaAvaliacao.setDadosEspecificos(jsonDados);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter dados da avaliação para JSON", e);
        }

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);

        return AvaliacaoResponse.fromEntity(avaliacaoSalva, objectMapper);
    }

    public List<AvaliacaoResponse> buscarPorPacienteId(Long pacienteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByPacienteId(pacienteId);
        
        return avaliacoes.stream()
                .map(av -> AvaliacaoResponse.fromEntity(av, objectMapper))
                .collect(Collectors.toList());
    }

    public AvaliacaoResponse atualizarAvaliacao(Long avaliacaoId, AvaliacaoRequest request) {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada com o ID: " + avaliacaoId));

        ProfissionalTipo tipo = avaliacao.getTipoProfissional();
        AvaliacaoStrategy strategy = avaliacaoFactory.getStrategy(tipo);

        AvaliacaoDados dadosAtualizados = strategy.processarAvaliacao(request);

        try {
            String jsonDados = objectMapper.writeValueAsString(dadosAtualizados);
            avaliacao.setDadosEspecificos(jsonDados);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar JSON da avaliação", e);
        }

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
        
        return AvaliacaoResponse.fromEntity(avaliacaoSalva, objectMapper);
    }

    public void deletarAvaliacao(Long avaliacaoId) {
        if (!avaliacaoRepository.existsById(avaliacaoId)) {
            throw new EntityNotFoundException("Avaliação não encontrada com o ID: " + avaliacaoId);
        }
        avaliacaoRepository.deleteById(avaliacaoId);
    }
}