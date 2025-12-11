package org.example.controller;

import org.example.DTO.DietaCadastroRequest;
import org.example.DTO.DietaResponse;
import org.example.service.DietaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dietas")
@CrossOrigin(origins = "*")
public class DietaController {

    private final DietaService dietaService;

    public DietaController(DietaService dietaService) {
        this.dietaService = dietaService;
    }

    @PostMapping
    public ResponseEntity<DietaResponse> criarDieta(@RequestBody DietaCadastroRequest dietaRequest) {
        return ResponseEntity.ok(dietaService.criarDieta(dietaRequest));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<DietaResponse> buscarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(dietaService.buscarDietaPorPacienteId(pacienteId));
    }

    @PutMapping("/{dietaId}")
    public ResponseEntity<DietaResponse> atualizarDieta(@PathVariable Long dietaId,
                                                        @RequestBody DietaCadastroRequest dietaRequest) {
        return ResponseEntity.ok(dietaService.atualizarDieta(dietaId, dietaRequest));
    }

    @DeleteMapping("/{dietaId}")
    public ResponseEntity<Void> deletarDieta(@PathVariable Long dietaId) {
        dietaService.deletarDieta(dietaId);
        return ResponseEntity.noContent().build();
    }
}