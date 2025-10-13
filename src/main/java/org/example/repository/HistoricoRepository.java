package org.example.repository;

import org.example.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    Optional<Historico> findByPacienteId(Long pacienteId);
    
}
