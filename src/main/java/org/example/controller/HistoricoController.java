package org.example.controller;

import org.example.DTO.HistoricoRequest;
import org.example.DTO.HistoricoResponse;
import org.example.service.HistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api") // Rota base
public class HistoricoController {

    private final HistoricoService historicoService;

    @Autowired
    public HistoricoController(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    
    @PostMapping("/pacientes/{pacienteId}/historico")
    public ResponseEntity<HistoricoResponse> criarHistorico(
            @PathVariable Long pacienteId,
            @RequestBody HistoricoRequest historicoRequest
    ) {
        HistoricoResponse novoHistorico = historicoService.criarHistorico(pacienteId, historicoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoHistorico);
    }

    
    @GetMapping("/pacientes/{pacienteId}/historico")
    public ResponseEntity<HistoricoResponse> buscarHistoricoPorPaciente(@PathVariable Long pacienteId) {
        HistoricoResponse historico = historicoService.buscarPorPacienteId(pacienteId);
        return ResponseEntity.ok(historico);
    }


    @PutMapping("/historicos/{historicoId}")
    public ResponseEntity<HistoricoResponse> atualizarHistorico(
            @PathVariable Long historicoId,
            @RequestBody HistoricoRequest historicoRequest
    ) {
        HistoricoResponse historicoAtualizado = historicoService.atualizarHistorico(historicoId, historicoRequest);
        return ResponseEntity.ok(historicoAtualizado);
    }
}
