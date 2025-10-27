package org.example.controller;

import org.example.DTO.NutricionistaCadastroRequest; // Importar DTO de request
import org.example.DTO.NutricionistaResponse;       // Importar DTO de response
import org.example.model.Nutricionista; // Não é mais necessário para este método
import org.example.service.NutricionistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nutricionistas")
public class NutricionistaController {

    private final NutricionistaService nutricionistaService;

    @Autowired
    public NutricionistaController(NutricionistaService nutricionistaService) {
        this.nutricionistaService = nutricionistaService;
    }

    @PostMapping("/cadastro")

    public ResponseEntity<NutricionistaResponse> cadastrar(@RequestBody NutricionistaCadastroRequest dados) {
        NutricionistaResponse salvo = nutricionistaService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}