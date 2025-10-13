package org.example.repository;

import org.example.model.Refeicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefeicaoRepository extends JpaRepository<Refeicao, Long> {
    Optional<Refeicao> findByDietaId(Long dietaId);
}