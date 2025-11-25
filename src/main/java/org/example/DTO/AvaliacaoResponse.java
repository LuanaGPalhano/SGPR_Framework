package org.example.DTO;
import org.example.model.Avaliacao;
import java.time.LocalDate;

public record AvaliacaoResponse(Long id, double peso, double altura, double imc, double percentualGordura, double circunferenciaCintura, double circunferenciaQuadril, String observacoes, LocalDate dataMedida, Long pacienteId) { 
    public AvaliacaoResponse(Avaliacao avaliacao) {
        this(avaliacao.getId(), avaliacao.getPeso(), avaliacao.getAltura(), avaliacao.getImc(), avaliacao.getPercentualGordura(), avaliacao.getCircunferenciaCintura(), avaliacao.getCircunferenciaQuadril(), avaliacao.getObservacoes(), avaliacao.getDataMedida(), avaliacao.getPaciente().getId());
    }
}