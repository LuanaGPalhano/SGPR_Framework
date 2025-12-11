package org.example.controller;

import org.example.DTO.HistoricoRequest;
import org.example.DTO.HistoricoResponse;
import org.example.enums.ProfissionalTipo;
import org.example.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HistoricoController {

    private final HistoricoService historicoService;

    @Autowired
    public HistoricoController(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    // 1. CRIAR: Adicionado @RequestParam para definir o tipo (Nutri, Personal, etc)
    // Ex: POST /api/pacientes/1/historicos?tipo=NUTRICIONISTA
    @PostMapping("/pacientes/{pacienteId}/historicos")
    public ResponseEntity<HistoricoResponse> criarHistorico(
            @PathVariable Long pacienteId,
            @RequestParam ProfissionalTipo tipo,
            @RequestBody HistoricoRequest historicoRequest
    ) {
        HistoricoResponse novoHistorico = historicoService.criarHistorico(pacienteId, historicoRequest, tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoHistorico);
    }

    // 2. LISTAR TODOS: Retorna uma lista com todas as fichas do paciente
    // Ex: GET /api/pacientes/1/historicos
    @GetMapping("/pacientes/{pacienteId}/historicos")
    public ResponseEntity<List<HistoricoResponse>> listarTodosPorPaciente(@PathVariable Long pacienteId) {
        List<HistoricoResponse> historicos = historicoService.buscarTodosPorPaciente(pacienteId);
        return ResponseEntity.ok(historicos);
    }

    // 3. BUSCAR ESPECÍFICO: Retorna apenas a ficha de uma área
    // Ex: GET /api/pacientes/1/historicos/filtro?tipo=NUTRICIONISTA
    @GetMapping("/pacientes/{pacienteId}/historicos/filtro")
    public ResponseEntity<HistoricoResponse> buscarPorTipo(
            @PathVariable Long pacienteId,
            @RequestParam ProfissionalTipo tipo
    ) {
        HistoricoResponse historico = historicoService.buscarPorPacienteETipo(pacienteId, tipo);
        return ResponseEntity.ok(historico);
    }

    // 4. ATUALIZAR: Mantido (busca pelo ID do histórico)
    // Ex: PUT /api/historicos/10
    @PutMapping("/historicos/{historicoId}")
    public ResponseEntity<HistoricoResponse> atualizarHistorico(
            @PathVariable Long historicoId,
            @RequestBody HistoricoRequest historicoRequest
    ) {
        HistoricoResponse historicoAtualizado = historicoService.atualizarHistorico(historicoId, historicoRequest);
        return ResponseEntity.ok(historicoAtualizado);
    }

    // 5. DELETAR: Adicionado conforme atualizado no Service
    // Ex: DELETE /api/historicos/10
    @DeleteMapping("/historicos/{historicoId}")
    public ResponseEntity<Void> deletarHistorico(@PathVariable Long historicoId) {
        historicoService.deletarHistorico(historicoId);
        return ResponseEntity.noContent().build();
    }
}
