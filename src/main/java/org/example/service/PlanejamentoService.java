package org.example.service;

import java.util.List;

import org.example.model.Planejamento;
import org.example.repository.PlanejamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanejamentoService {
    private final PlanejamentoRepository repository;

    public PlanejamentoService(PlanejamentoRepository repository){
        this.repository = repository;
    }

     public Planejamento salvar(Planejamento planejamento) {
        return repository.save(planejamento);
    }

    public List<Planejamento> listarTodos() {
        return repository.findAll();
    }

    public void deletar(Long id){
       repository.deleteById(id);
    }

}