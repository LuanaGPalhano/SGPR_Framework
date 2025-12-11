package org.example.repository;

import org.example.model.DiarioAlimentar;
import org.springframework.data.jpa.repository.*;

public interface DiarioAlimentarRepository extends JpaRepository<DiarioAlimentar, Long> {
}
