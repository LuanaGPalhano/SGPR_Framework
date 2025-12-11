package org.example.service;

import java.util.List;

import org.example.DTO.PlanejamentoRequest;
import org.example.model.EntradaPlanejamento;
import org.example.model.ItemRefeicao;
import org.example.model.Planejamento;
import org.example.repository.PacienteRepository;
import org.example.repository.PlanejamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanejamentoService {
    private final PlanejamentoRepository planejamentoRepository;
    private final PacienteRepository pacienteRepository;

    public PlanejamentoService(PlanejamentoRepository planejamentoRepository, PacienteRepository pacienteRepository){
        this.planejamentoRepository = planejamentoRepository;
        this.pacienteRepository = pacienteRepository;
    }

     public Planejamento salvar(PlanejamentoRequest request) {
        Planejamento planejamento = Planejamento.builder()
        .descricao(request.descricao())
        .build();

        List<EntradaPlanejamento> entradas = request.entradas().stream()
        .map(entradaRequest -> {
            EntradaPlanejamento entrada = EntradaPlanejamento.builder()
            .dia(entradaRequest.dia())
            .refeicao(entradaRequest.refeicao())
            .planejamento(planejamento)
            .build();

        List<ItemRefeicao> itens = entradaRequest.porcao().stream()
        .map(itemRequest -> ItemRefeicao.builder()
            .alimento(itemRequest.alimento())
            .quantidade(Integer.parseInt(itemRequest.quantidade()))
            .entrada(entrada)
            .build())
        .toList();

        entrada.setItensRefeicao(itens);
        return entrada;
        }).toList();

        planejamento.setEntradas(entradas);

        return planejamentoRepository.save(planejamento);
    }

    public List<Planejamento> listarTodos() {
        return planejamentoRepository.findAll();
    }

    public void deletar(Long id){
       planejamentoRepository.deleteById(id);
    }

}