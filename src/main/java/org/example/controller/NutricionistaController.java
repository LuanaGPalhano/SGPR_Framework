package org.example.controller;


import org.example.model.Nutricionista;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.service.NutricionistaService;

@RestController
@RequestMapping("/api/nutricionistas")
public class NutricionistaController {

    private final NutricionistaService nutricionistaService;

    public NutricionistaController(NutricionistaService nutricionistaService) {
        this.nutricionistaService = nutricionistaService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Nutricionista> cadastrar(@RequestBody Nutricionista nutricionista) {

        Nutricionista salvo = nutricionistaService.cadastrar(nutricionista);
        return ResponseEntity.ok(salvo);
    }
}