package org.example.controller;

import org.example.DTO.ProfissionalCadastroRequest;
import org.example.DTO.ProfissionalResponse;
import org.example.service.ProfissionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profissionais") // Endpoint genérico
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    /**
     * PONTO FIXO: Cadastro de qualquer profissional.
     * O JSON de entrada define se é NUTRICIONISTA, EDUCADOR_FISICO ou PSICOLOGO.
     */
    @PostMapping("/cadastro")
    public ResponseEntity<ProfissionalResponse> cadastrar(@RequestBody ProfissionalCadastroRequest dados) {
        ProfissionalResponse salvo = profissionalService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /**
     * PONTO FIXO: Associação de paciente.
     * Funciona para qualquer profissional, pois a lista de pacientes está na classe pai.
     */
    @PutMapping("/{profissionalId}/associar-paciente/{pacienteId}")
    public ResponseEntity<Void> associarPaciente(
            @PathVariable Long profissionalId,
            @PathVariable Long pacienteId) {

        profissionalService.associarPaciente(profissionalId, pacienteId);
        return ResponseEntity.ok().build();
    }
}