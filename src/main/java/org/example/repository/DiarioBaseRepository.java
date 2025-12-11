package org.example.repository;

import org.example.model.DiarioBase;
import org.springframework.data.jpa.repository.*;

import java.util.List;
public interface DiarioBaseRepository extends JpaRepository<DiarioBase, Long> {
    List<DiarioBase> findByPacienteId(Long pacienteId);
}
