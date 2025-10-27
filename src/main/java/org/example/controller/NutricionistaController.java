package org.example.controller;

import org.example.model.Nutricionista;
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

    @PostMapping("/cadastro") // Endpoint específico para cadastro
    public ResponseEntity<Nutricionista> cadastrar(@RequestBody Nutricionista nutricionista) {
        Nutricionista salvo = nutricionistaService.cadastrar(nutricionista);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}