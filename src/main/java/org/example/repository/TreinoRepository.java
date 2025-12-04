package org.example.repository;

import org.example.model.Treino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long> {

    // ADICIONE ESTE MÉTODO:
    // Busca todos os treinos vinculados a um paciente
    List<Treino> findByPacienteId(Long pacienteId);
}