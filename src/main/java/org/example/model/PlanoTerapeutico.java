package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "planos_terapeuticos")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlanoTerapeutico extends Plano { // Herda de Plano

    // Campos específicos da Psicologia
    private String tipoAbordagem; // Ex: TCC, Psicanálise

    @Column(columnDefinition = "TEXT")
    private String anotacoesSessao;

    // RELACIONAMENTO COM O PROFISSIONAL
    @ManyToOne
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;
}