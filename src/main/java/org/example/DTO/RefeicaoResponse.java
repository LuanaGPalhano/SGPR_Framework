package org.example.DTO;

import org.example.model.Refeicao;

public record RefeicaoResponse(Long id, String nome, String descricao, Long dietaId) {
    public RefeicaoResponse(Refeicao refeicao) {
        this(refeicao.getId(), refeicao.getNome(), refeicao.getDescricao(), refeicao.getDieta().getId());
    }
    
}
