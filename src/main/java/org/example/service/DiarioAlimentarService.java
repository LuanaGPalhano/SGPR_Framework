package org.example.service;

import java.util.List;
import org.springframework.stereotype.*;
import org.example.model.DiarioAlimentar;
import org.example.repository.DiarioAlimentarRepository;

@Service
public class DiarioAlimentarService {
    private final DiarioAlimentarRepository repository;

    public DiarioAlimentarService(DiarioAlimentarRepository repository){
        this.repository = repository;
    }

    public DiarioAlimentar salvar(DiarioAlimentar diario) {
        return repository.save(diario);
    }

    public List<DiarioAlimentar> listarTodos() {
        return repository.findAll();
    }

}