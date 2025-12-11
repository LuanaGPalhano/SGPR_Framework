package org.example.repository;

import org.example.model.Dieta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietaRepository extends JpaRepository<Dieta, Long> {

    @Query("SELECT d FROM Dieta d " +
            "LEFT JOIN FETCH d.refeicoes r " +
            "LEFT JOIN FETCH r.itensRefeicao " +
            "WHERE d.paciente.id = :pacienteId")
        // O NOME CORRETO É ESTE:
    List<Dieta> findAllByPacienteIdWithDetails(@Param("pacienteId") Long pacienteId);
}