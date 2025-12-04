package org.example.repository;

import org.example.model.PlanoTerapeutico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanoTerapeuticoRepository extends JpaRepository<PlanoTerapeutico, Long> {

    // Busca os planos do paciente para a estratégia de Rotina
    List<PlanoTerapeutico> findByPacienteId(Long pacienteId);
}