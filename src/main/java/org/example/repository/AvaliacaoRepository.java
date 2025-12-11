package org.example.repository;

import org.example.enums.ProfissionalTipo;
import org.example.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByPacienteId(Long pacienteId);

    List<Avaliacao> findByPacienteIdOrderByDataAvaliacaoDesc(Long pacienteId);

    List<Avaliacao> findByPacienteIdAndTipoProfissional(Long pacienteId, ProfissionalTipo tipoProfissional);
    
    List<Avaliacao> findByPacienteIdAndTipoProfissionalOrderByDataAvaliacaoDesc(Long pacienteId, ProfissionalTipo tipoProfissional);
}