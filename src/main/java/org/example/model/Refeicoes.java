package org.example.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "refeicoes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Refeicoes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conjuntoRefeicoes;

    @ManyToOne
    @JoinColumn(name = "diario_id")
    private DiarioAlimentar diario;
}

