package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double peso;
    private double altura;
    private double imc;
    private double percentualGordura;
    private double circunferenciaCintura;
    private double circunferenciaQuadril;
    private String observacoes;

    @OneToOne(mappedBy = "avaliacao")
    private Paciente paciente;

    @OneToOne(mappedBy = "avaliacao")
    private Historico historico;

    public Avaliacao(double peso, double altura, double percentualGordura, double circunferenciaCintura, double circunferenciaQuadril, String observacoes) {
        this.peso = peso;
        this.altura = altura;
        this.imc = peso / (altura * altura);
        this.percentualGordura = percentualGordura;
        this.circunferenciaCintura = circunferenciaCintura;
        this.circunferenciaQuadril = circunferenciaQuadril;
        this.observacoes = observacoes;
    }

}
