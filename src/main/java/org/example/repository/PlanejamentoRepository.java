package org.example.repository;

import org.example.model.Planejamento;
import org.springframework.data.jpa.repository.*;

public interface PlanejamentoRepository extends JpaRepository<Planejamento, Long> {
    
}
