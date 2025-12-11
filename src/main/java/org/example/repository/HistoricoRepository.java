package org.example.repository;

import org.example.enums.ProfissionalTipo;
import org.example.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findByPacienteId(Long pacienteId);

    Optional<Historico> findByPacienteIdAndTipoProfissional(Long pacienteId, ProfissionalTipo tipoProfissional);

    boolean existsByPacienteIdAndTipoProfissional(Long pacienteId, ProfissionalTipo tipoProfissional);
}

