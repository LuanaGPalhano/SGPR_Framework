package org.example.service;

import jakarta.transaction.Transactional;
import org.example.DTO.*;
import org.example.model.*;
import org.example.repository.DietaRepository;
import org.example.repository.NutricionistaRepository;
import org.example.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietaService {

    private final DietaRepository dietaRepository;
    private final NutricionistaRepository nutricionistaRepository;
    private final PacienteRepository pacienteRepository;

    public DietaService(DietaRepository dietaRepository,
                        NutricionistaRepository nutricionistaRepository,
                        PacienteRepository pacienteRepository) {
        this.dietaRepository = dietaRepository;
        this.nutricionistaRepository = nutricionistaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    /* ======================== CRIAÇÃO ======================== */
    @Transactional
    public DietaResponse criarDieta(DietaCadastroRequest request) {
        Dieta dieta = mapearParaEntidade(request);
        Dieta salva = dietaRepository.save(dieta);
        return mapearParaResponse(salva);
    }

    /* ======================== BUSCA ======================== */
    public DietaResponse buscarDietaPorPacienteId(Long pacienteId) {

        // 1. Busca a lista
        Dieta dieta = dietaRepository.findAllByPacienteIdWithDetails(pacienteId)
                .stream() // Converte a lista para um Stream
                .findFirst() // Tenta pegar o primeiro item
                .orElseThrow(() -> new RuntimeException("Dieta não encontrada para o paciente ID: " + pacienteId)); // Lança erro se a lista estava vazia

        // 2. Mapeia o único item encontrado
        return mapearParaResponse(dieta);
    }

    /* ======================== ATUALIZAÇÃO ======================== */
    @Transactional
    public DietaResponse atualizarDieta(Long dietaId, DietaCadastroRequest request) {
        Dieta existente = dietaRepository.findById(dietaId)
                .orElseThrow(() -> new RuntimeException("Dieta não encontrada com ID: " + dietaId));

        existente.setDataInicio(request.dataInicio());
        existente.setDataFim(request.dataFim());
        existente.setObjetivo(request.objetivo());

        if (request.nutricionistaId() != null) {
            existente.setNutricionista(nutricionistaRepository.findById(request.nutricionistaId())
                    .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado")));
        }

        if (request.pacienteId() != null) {
            existente.setPaciente(pacienteRepository.findById(request.pacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado")));
        }

        // Atualizar lista de refeições e itens
        existente.getRefeicoes().clear();
        if (request.entradaDieta() != null) {
            request.entradaDieta().forEach(ref -> {
                Refeicao refeicao = new Refeicao();
                refeicao.setNome(ref.nome());
                refeicao.setHorario(ref.horario());
                refeicao.setDescricao(ref.descricao());
                refeicao.setDieta(existente);

                if (ref.itens() != null) {
                    List<ItemRefeicao> itens = ref.itens().stream().map(itemReq -> {
                        ItemRefeicao item = new ItemRefeicao();
                        item.setAlimento(itemReq.alimento());
                        item.setQuantidade(itemReq.quantidade());
                        item.setUnidadeMedida(itemReq.unidadeMedida());
                        item.setResumoNutricional(itemReq.resumoNutricional());
                        item.setRefeicao(refeicao);
                        return item;
                    }).collect(Collectors.toList());
                    refeicao.setItens(itens);
                }
                existente.getRefeicoes().add(refeicao);
            });
        }

        Dieta atualizada = dietaRepository.save(existente);
        return mapearParaResponse(atualizada);
    }

    /* ======================== EXCLUSÃO ======================== */
    public void deletarDieta(Long dietaId) {
        dietaRepository.deleteById(dietaId);
    }

    /* ======================== MAPEAMENTO DTO <-> ENTIDADE ======================== */
    private Dieta mapearParaEntidade(DietaCadastroRequest dto) {
        Dieta dieta = new Dieta();
        dieta.setDataInicio(dto.dataInicio());
        dieta.setDataFim(dto.dataFim());
        dieta.setObjetivo(dto.objetivo());

        if (dto.nutricionistaId() != null) {
            dieta.setNutricionista(nutricionistaRepository.findById(dto.nutricionistaId())
                    .orElseThrow(() -> new RuntimeException("Nutricionista não encontrado")));
        }

        if (dto.pacienteId() != null) {
            dieta.setPaciente(pacienteRepository.findById(dto.pacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado")));
        }

        if (dto.entradaDieta() != null) {
            List<Refeicao> refeicoes = dto.entradaDieta().stream().map(ref -> {
                Refeicao r = new Refeicao();
                r.setNome(ref.nome());
                r.setHorario(ref.horario());
                r.setDescricao(ref.descricao());
                r.setDieta(dieta);

                if (ref.itens() != null) {
                    List<ItemRefeicao> itens = ref.itens().stream().map(itemReq -> {
                        ItemRefeicao i = new ItemRefeicao();
                        i.setAlimento(itemReq.alimento());
                        i.setQuantidade(itemReq.quantidade());
                        i.setUnidadeMedida(itemReq.unidadeMedida());
                        i.setResumoNutricional(itemReq.resumoNutricional());
                        i.setRefeicao(r);
                        return i;
                    }).collect(Collectors.toList());
                    r.setItens(itens);
                }

                return r;
            }).collect(Collectors.toList());
            dieta.setRefeicoes(refeicoes);
        }

        return dieta;
    }

    private DietaResponse mapearParaResponse(Dieta dieta) {
        List<RefeicaoResponse> refeicoes = dieta.getRefeicoes() != null
                ? dieta.getRefeicoes().stream().map(ref -> new RefeicaoResponse(
                ref.getId(),
                ref.getNome(),
                ref.getHorario(),
                ref.getDescricao(),
                ref.getItens() != null
                        ? ref.getItens().stream()
                        .map(item -> new ItemRefeicaoResponse(
                                item.getId(),
                                item.getAlimento(),
                                item.getQuantidade(),
                                item.getUnidadeMedida(),
                                item.getResumoNutricional()
                        )).collect(Collectors.toList())
                        : List.of()
        )).collect(Collectors.toList())
                : List.of();

        return new DietaResponse(
                dieta.getId(),
                dieta.getDataInicio(),
                dieta.getDataFim(),
                dieta.getObjetivo(),
                dieta.getNutricionista() != null ? dieta.getNutricionista().getId() : null,
                dieta.getPaciente() != null ? dieta.getPaciente().getId() : null,
                refeicoes
        );
    }
}