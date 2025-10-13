package org.example.service;

import org.example.model.Dieta;
import org.example.repository.DietaRepository;
import org.springframework.stereotype.Service;

import java.util.List; // Importe a classe List

@Service
public class DietaService {
    private final DietaRepository dietaRepository;

    public DietaService(DietaRepository dietaRepository) {
        this.dietaRepository = dietaRepository;
    }

    public Dieta criarDieta(Dieta dieta) {
        return dietaRepository.save(dieta);
    }

    // --- MÉTODO CORRIGIDO ---
    public List<Dieta> buscarDietasPorPacienteId(Long pacienteId) {
        // 1. Chame o método com o nome correto
        List<Dieta> dietas = dietaRepository.findAllByPacienteIdWithDetails(pacienteId);

        // 2. Verifique se a lista está vazia
        if (dietas.isEmpty()) {
            throw new RuntimeException("Nenhuma dieta encontrada para o paciente ID: " + pacienteId);
        }

        // 3. Retorne a lista completa de dietas
        return dietas;
    }

    public void deletarDieta(Long dietaId) {
        dietaRepository.deleteById(dietaId);
    }

    public Dieta atualizarDieta(Long dietaId, Dieta dietaAtualizada) {
        Dieta dietaExistente = dietaRepository.findById(dietaId)
                .orElseThrow(() -> new RuntimeException("Dieta não encontrada com ID: " + dietaId));

        if (dietaAtualizada.getRefeicoes() != null) {
            dietaExistente.getRefeicoes().clear(); // Limpa as refeições antigas
            dietaExistente.getRefeicoes().addAll(dietaAtualizada.getRefeicoes()); // Adiciona as novas
            dietaExistente.getRefeicoes().forEach(refeicao -> refeicao.setDieta(dietaExistente));
        }

        return dietaRepository.save(dietaExistente);
    }
}