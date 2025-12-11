package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.DTO.HistoricoRequest;
import org.example.DTO.HistoricoResponse;
import org.example.enums.ProfissionalTipo;
import org.example.model.Historico;
import org.example.model.HistoricoDados;
import org.example.model.Paciente;
import org.example.repository.HistoricoRepository;
import org.example.repository.PacienteRepository;
import org.example.strategy.HistoricoFactoryService;
import org.example.strategy.HistoricoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final PacienteRepository pacienteRepository;
    
    // Novas dependências para a lógica Flexível
    private final HistoricoFactoryService historicoFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public HistoricoService(HistoricoRepository historicoRepository,
                            PacienteRepository pacienteRepository,
                            HistoricoFactoryService historicoFactory,
                            ObjectMapper objectMapper) {
        this.historicoRepository = historicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.historicoFactory = historicoFactory;
        this.objectMapper = objectMapper;
    }

    // 1. CRIAR: Agora exige o Tipo do Profissional
    public HistoricoResponse criarHistorico(Long pacienteId, HistoricoRequest request, ProfissionalTipo tipo) {
        
        // Verifica se o paciente existe
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID: " + pacienteId));

        // (Opcional) Verifica se já existe um histórico desse tipo para não duplicar
        if (historicoRepository.findByPacienteIdAndTipoProfissional(pacienteId, tipo).isPresent()) {
             throw new IllegalArgumentException("Já existe um histórico de " + tipo + " para este paciente. Use a edição.");
        }

        Historico historico = new Historico();
        historico.setPaciente(paciente);
        historico.setTipoProfissional(tipo);
        historico.setDataPreenchimento(LocalDate.now());

        // --- A. Preenche Pontos Fixos (Comuns) ---
        historico.setEstadoCivil(request.estadoCivil());
        historico.setOcupacao(request.ocupacao());
        historico.setBebe(request.bebe());
        historico.setFuma(request.fuma());

        // --- B. Preenche Pontos Variáveis (Strategy + JSON) ---
        HistoricoStrategy strategy = historicoFactory.getStrategy(tipo);
        HistoricoDados dadosProcessados = strategy.processarHistorico(request);

        try {
            String json = objectMapper.writeValueAsString(dadosProcessados);
            historico.setDadosEspecificos(json);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar dados específicos do histórico", e);
        }

        Historico salvo = historicoRepository.save(historico);
        return HistoricoResponse.fromEntity(salvo, objectMapper);
    }

    // 2. BUSCAR TODOS: Retorna lista (Nutrição, Treino, etc.)
    public List<HistoricoResponse> buscarTodosPorPaciente(Long pacienteId) {
        return historicoRepository.findByPacienteId(pacienteId)
                .stream()
                .map(h -> HistoricoResponse.fromEntity(h, objectMapper))
                .collect(Collectors.toList());
    }

    // 3. BUSCAR ESPECÍFICO: Usado quando o Nutricionista abre a tela
    public HistoricoResponse buscarPorPacienteETipo(Long pacienteId, ProfissionalTipo tipo) {
        Historico historico = historicoRepository.findByPacienteIdAndTipoProfissional(pacienteId, tipo)
                .orElseThrow(() -> new EntityNotFoundException("Ficha de " + tipo + " não encontrada para este paciente."));
        
        return HistoricoResponse.fromEntity(historico, objectMapper);
    }

    // 4. ATUALIZAR: Respeita o tipo original do registro
    public HistoricoResponse atualizarHistorico(Long historicoId, HistoricoRequest request) {
        Historico historico = historicoRepository.findById(historicoId)
                .orElseThrow(() -> new EntityNotFoundException("Histórico não encontrado com o ID: " + historicoId));

        // Atualiza Pontos Fixos
        historico.setEstadoCivil(request.estadoCivil());
        historico.setOcupacao(request.ocupacao());
        historico.setBebe(request.bebe());
        historico.setFuma(request.fuma());

        // Atualiza Pontos Variáveis
        // Descobre qual estratégia usar olhando para o banco (ex: Se era Nutrição, continua Nutrição)
        ProfissionalTipo tipo = historico.getTipoProfissional();
        HistoricoStrategy strategy = historicoFactory.getStrategy(tipo);
        
        HistoricoDados dadosNovos = strategy.processarHistorico(request);

        try {
            String json = objectMapper.writeValueAsString(dadosNovos);
            historico.setDadosEspecificos(json);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar JSON do histórico", e);
        }

        Historico atualizado = historicoRepository.save(historico);
        return HistoricoResponse.fromEntity(atualizado, objectMapper);
    }
    
    public void deletarHistorico(Long historicoId) {
        if (!historicoRepository.existsById(historicoId)) {
            throw new EntityNotFoundException("Histórico não encontrado com ID: " + historicoId);
        }
        historicoRepository.deleteById(historicoId);
    }
}



