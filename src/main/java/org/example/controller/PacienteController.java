package org.example.controller;

import org.example.DTO.PacienteCadastroRequest;
import org.example.DTO.PacienteResponse;
import org.example.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cadastrar")
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/paciente") 
    public ResponseEntity<PacienteResponse> cadastrar(@RequestBody PacienteCadastroRequest dados) {
        PacienteResponse pacienteSalvo = pacienteService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteSalvo);
    }
}
