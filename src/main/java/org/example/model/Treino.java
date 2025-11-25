package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "treinos")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Treino extends Plano { // Herda de Plano (que tem ID, objetivo, etc.)

    private String tipoTreino; // Ex: Hipertrofia, Cardio

    @Column(columnDefinition = "TEXT")
    private String descricaoExercicios;

    @ManyToOne
    @JoinColumn(name = "educador_fisico_id")
    private EducadorFisico educadorFisico;
}
