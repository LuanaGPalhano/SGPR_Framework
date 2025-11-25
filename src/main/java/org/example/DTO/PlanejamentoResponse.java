package org.example.DTO;

import org.example.model.Planejamento;
import org.example.model.EntradaPlanejamento;
import org.example.model.ItemRefeicao;

import java.util.List;

public record PlanejamentoResponse(
    Long id,
    String descricao,
    List<EntradaResponse> entradas
) {

    public PlanejamentoResponse(Planejamento planejamento){
        this(planejamento.getId(), planejamento.getDescricao(), planejamento.getEntradas()
        .stream()
        .map(EntradaResponse::new)
        .toList());
    }

    public record EntradaResponse(
        Long id,
        String dia,
        String refeicao,
        List<ItemRefeicaoResponse> porcao
    ){
        public EntradaResponse(EntradaPlanejamento entrada){
            this(entrada.getId(), entrada.getDia(), entrada.getRefeicao(), entrada.getItensRefeicao()
            .stream()
            .map(ItemRefeicaoResponse::new)
            .toList());
        }
    }

    public record ItemRefeicaoResponse(
        String alimento,
        String quantidade
    ){
        public ItemRefeicaoResponse(ItemRefeicao itemRefeicao){
            this(itemRefeicao.getAlimento(), String.valueOf(itemRefeicao.getQuantidade()));
        }
    }
}