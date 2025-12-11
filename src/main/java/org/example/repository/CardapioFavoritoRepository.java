package org.example.repository;

import org.example.model.CardapioFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardapioFavoritoRepository extends JpaRepository<CardapioFavorito, Long> {

    // Método para buscar todos os favoritos de um paciente específico
    List<CardapioFavorito> findByPacienteId(Long pacienteId);
}