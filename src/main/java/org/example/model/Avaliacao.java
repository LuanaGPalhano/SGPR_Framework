package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
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
    private LocalDate dataMedida;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

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
