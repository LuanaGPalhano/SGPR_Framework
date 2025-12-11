package org.example.controller;

import org.example.model.DiarioBase;
import org.example.service.DiarioBaseService;
import org.springframework.web.bind.annotation.*;
import org.example.DTO.DiarioBaseRequest;
import org.example.template.DiarioTipo;

import java.util.List;

@RestController
@RequestMapping("/diario")
@CrossOrigin(origins = {"http://localhost:5501", "http://127.0.0.1:5501"})
public class DiarioBaseController {

    private final DiarioBaseService service;

    public DiarioBaseController(DiarioBaseService service) {
        this.service = service;
    }

    @PostMapping
    public DiarioBase salvar(@RequestBody DiarioBaseRequest diarioRequest, @RequestParam("tipo") DiarioTipo tipo) {
        return service.salvar(diarioRequest, tipo);
    }

    @GetMapping
    public List<DiarioBase> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("paciente/{cpf}")
    public List<DiarioBase> listarPorPacienteCpf(@PathVariable String cpf){
        return service.listarPorPacienteCpf(cpf);
    }
}