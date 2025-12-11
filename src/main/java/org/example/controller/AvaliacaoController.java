package org.example.controller;

import org.example.DTO.AvaliacaoRequest;
import org.example.DTO.AvaliacaoResponse;
import org.example.enums.ProfissionalTipo;
import org.example.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @Autowired
    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    // Exemplo de URL: POST /api/avaliacoes/pacientes/1?tipo=NUTRICIONISTA
    @PostMapping("/pacientes/{pacienteId}")
    public ResponseEntity<AvaliacaoResponse> criar(
            @PathVariable Long pacienteId,
            @RequestParam ProfissionalTipo tipo, 
            @RequestBody AvaliacaoRequest request) {
            
        AvaliacaoResponse novaAvaliacao = avaliacaoService.criarAvaliacao(pacienteId, request, tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<AvaliacaoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<AvaliacaoResponse> avaliacoes = avaliacaoService.buscarPorPacienteId(pacienteId);
        return ResponseEntity.ok(avaliacoes);
    }

    @PutMapping("/{avaliacaoId}")
    public ResponseEntity<AvaliacaoResponse> atualizar(
            @PathVariable Long avaliacaoId, 
            @RequestBody AvaliacaoRequest request) {
            
        AvaliacaoResponse avaliacaoAtualizada = avaliacaoService.atualizarAvaliacao(avaliacaoId, request);
        return ResponseEntity.ok(avaliacaoAtualizada);
    }

    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> deletar(@PathVariable Long avaliacaoId) {
        avaliacaoService.deletarAvaliacao(avaliacaoId);
        return ResponseEntity.noContent().build();
    }
}
