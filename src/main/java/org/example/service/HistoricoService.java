package org.example.service;

import org.example.model.Historico;
import org.example.repository.HistoricoRepository;
import org.springframework.stereotype.Service;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    public HistoricoService(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    public Historico getHistoricoByPacienteId(Long pacienteId) {
        return historicoRepository.findByPacienteId(pacienteId)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado para o paciente ID: " + pacienteId));
    }

    public Historico criarHistorico(Historico historico) {
        return historicoRepository.save(historico);
    }

    public Historico atualizarHistorico(Long historicoId, Historico historicoAtualizado) {
        Historico historicoExistente = historicoRepository.findById(historicoId)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado com ID: " + historicoId));

        historicoExistente.setEstadoCivil(historicoAtualizado.getEstadoCivil());
        historicoExistente.setOcupacao(historicoAtualizado.getOcupacao());
        historicoExistente.setAlergias(historicoAtualizado.getAlergias());
        historicoExistente.setMedicamentos(historicoAtualizado.getMedicamentos());
        historicoExistente.setSuplementacao(historicoAtualizado.getSuplementacao());
        historicoExistente.setHistoricoFamiliar(historicoAtualizado.getHistoricoFamiliar());
        historicoExistente.setOutrasCondicoes(historicoAtualizado.getOutrasCondicoes());
        historicoExistente.setBebe(historicoAtualizado.isBebe());
        historicoExistente.setFuma(historicoAtualizado.isFuma());
        // Se tiver mais campos, atualize aqui também

        return historicoRepository.save(historicoExistente);
    }

    public void deletarHistorico(Long historicoId) {
        if (!historicoRepository.existsById(historicoId)) {
            throw new RuntimeException("Histórico não encontrado com ID: " + historicoId);
        }
        historicoRepository.deleteById(historicoId);
    }
}


