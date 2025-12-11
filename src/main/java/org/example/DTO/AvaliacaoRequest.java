package org.example.DTO;
import java.time.LocalDate;

public record AvaliacaoRequest(
    double peso,
    double altura,
    double percentualGordura,
    double circunferenciaCintura,
    double circunferenciaQuadril,
    String observacoes
) {
}