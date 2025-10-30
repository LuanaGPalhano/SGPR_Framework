package org.example.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "entrada_diario")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class EntradaDiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "diario_id")
    private DiarioAlimentar diario;
}

