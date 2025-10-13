package org.example.repository;

import org.example.model.ItemRefeicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRefeicaoRepository extends JpaRepository<ItemRefeicao, Long> {
    Optional<ItemRefeicao> findByRefeicaoId(Long refeicaoId);
}
