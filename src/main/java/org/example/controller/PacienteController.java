package org.example.controller;

import org.example.DTO.PacienteCadastroRequest;
import org.example.DTO.PacienteResponse;
import org.example.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<PacienteResponse> cadastrar(@RequestBody PacienteCadastroRequest dados) {
        PacienteResponse pacienteSalvo = pacienteService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteSalvo);
    }
}