package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.DTO.AvaliacaoRequest;
import org.example.DTO.AvaliacaoResponse;
import org.example.model.Avaliacao;
import org.example.model.Paciente;
import org.example.repository.AvaliacaoRepository;
import org.example.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PacienteRepository pacienteRepository;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, PacienteRepository pacienteRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    
    public AvaliacaoResponse criarAvaliacao(Long pacienteId, AvaliacaoRequest request) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado com o ID: " + pacienteId));

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setPaciente(paciente);
        
        novaAvaliacao.setPeso(request.peso());
        novaAvaliacao.setAltura(request.altura());
        novaAvaliacao.setPercentualGordura(request.percentualGordura());
        novaAvaliacao.setCircunferenciaCintura(request.circunferenciaCintura());
        novaAvaliacao.setCircunferenciaQuadril(request.circunferenciaQuadril());
        novaAvaliacao.setObservacoes(request.observacoes());

        novaAvaliacao.setImc(calcularImc(request.peso(), request.altura()));
        novaAvaliacao.setDataMedida(LocalDate.now());

        Avaliacao avaliacaoSalva = avaliacaoRepository.save(novaAvaliacao);

        // Retorna um DTO de resposta
        return new AvaliacaoResponse(avaliacaoSalva);
    }

   
    public List<AvaliacaoResponse> buscarPorPacienteId(Long pacienteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByPacienteId(pacienteId);
        
        // Converte a lista de entidades para uma lista de DTOs de resposta
        return avaliacoes.stream()
                .map(AvaliacaoResponse::new)
                .collect(Collectors.toList());
    }

    
    public AvaliacaoResponse atualizarAvaliacao(Long avaliacaoId, AvaliacaoRequest request) {
        // 1. Busca a avaliação existente ou lança um erro
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada com o ID: " + avaliacaoId));

        // 2. Atualiza os campos com os novos dados
        avaliacao.setPeso(request.peso());
        avaliacao.setAltura(request.altura());
        avaliacao.setPercentualGordura(request.percentualGordura());
        avaliacao.setCircunferenciaCintura(request.circunferenciaCintura());
        avaliacao.setCircunferenciaQuadril(request.circunferenciaQuadril());
        avaliacao.setObservacoes(request.observacoes());
        avaliacao.setImc(calcularImc(request.peso(), request.altura()));

        // 3. Salva as alterações
        Avaliacao avaliacaoAtualizada = avaliacaoRepository.save(avaliacao);
        return new AvaliacaoResponse(avaliacaoAtualizada);
    }

    public void deletarAvaliacao(Long avaliacaoId) {
        if (!avaliacaoRepository.existsById(avaliacaoId)) {
            throw new EntityNotFoundException("Avaliação não encontrada com o ID: " + avaliacaoId);
        }
        avaliacaoRepository.deleteById(avaliacaoId);
    }
    
    
    private double calcularImc(double peso, double altura) {
        if (altura <= 0) {
            return 0; // Evita divisão por zero
        }
        // Fórmula do IMC
        return peso / (altura * altura);
    }
}
