package org.example.repository;


import org.example.model.Nutricionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutricionistaRepository extends JpaRepository<Nutricionista, Long> {
    Optional<Nutricionista> findByCrnUf(String crnUf);}

