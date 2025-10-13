package org.example.controller;

import org.example.model.DiarioAlimentar;
import org.example.service.DiarioAlimentarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diario")
//@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class DiarioAlimentarController {

    private final DiarioAlimentarService service;

    public DiarioAlimentarController(DiarioAlimentarService service) {
        this.service = service;
    }

    @PostMapping
    public DiarioAlimentar salvar(@RequestBody DiarioAlimentar diario) {
        return service.salvar(diario);
    }

    @GetMapping
    public List<DiarioAlimentar> listarTodos() {
        return service.listarTodos();
    }
}