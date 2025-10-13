package org.example.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "item_refeicao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRefeicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String alimento;
    private double quantidade;
    private String unidadeMedida;
    private double calorias;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "refeicao_id")
    private Refeicao refeicao;

}
