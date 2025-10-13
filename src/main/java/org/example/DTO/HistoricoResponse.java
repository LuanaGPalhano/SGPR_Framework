package org.example.DTO;

import org.example.model.Historico;

public record HistoricoResponse (Long id, String estadoCiviil, String ocupacao, String alergias, String medicamentos, String suplementacao, String historicoFamiliar, String outrasCondicoes, boolean bebe, boolean fuma, Long pacienteId, Long avaliacaoId) {
    public HistoricoResponse(Historico historico) {
        this(historico.getId(), historico.getEstadoCivil(), historico.getOcupacao(), historico.getAlergias(), historico.getMedicamentos(), historico.getSuplementacao(), historico.getHistoricoFamiliar(), historico.getOutrasCondicoes(), historico.isBebe(), historico.isFuma(), historico.getPaciente() != null ? historico.getPaciente().getId() : null,
        historico.getAvaliacao() != null ? historico.getAvaliacao().getId() : null);
    }
    
}
