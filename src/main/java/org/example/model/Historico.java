package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historico")
@Data
@NoArgsConstructor
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estadoCivil;
    private String ocupacao;
    private String alergias;
    private String medicamentos;
    private String suplementacao;
    private String historicoFamiliar;
    private String outrasCondicoes;
    private boolean bebe;
    private boolean fuma;

    @OneToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;


    public Historico(String estadoCivil, String ocupacao, String alergias, String medicamentos, String suplementacao, String historicoFamiliar, String outrasCondicoes, boolean isBebe, boolean isFuma) {
        this.estadoCivil = estadoCivil;
        this.ocupacao = ocupacao;
        this.alergias = alergias;
        this.medicamentos = medicamentos;
        this.suplementacao = suplementacao;
        this.historicoFamiliar = historicoFamiliar;
        this.outrasCondicoes = outrasCondicoes;
        this.bebe = isBebe;
        this.fuma = isFuma;
    }



}
