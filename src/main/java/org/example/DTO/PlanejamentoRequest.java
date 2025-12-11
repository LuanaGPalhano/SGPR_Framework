package org.example.DTO;

import java.util.List;

public record PlanejamentoRequest(
    String descricao,
    List<EntradaRequest> entradas
){
    public record EntradaRequest(
        String dia,
        String refeicao,
        List<ItemRefeicaoRequest> porcao
    ){}
    public record ItemRefeicaoRequest(
        String alimento,
        String quantidade
    ){}
}