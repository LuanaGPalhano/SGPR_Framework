package org.example.DTO;

public record HistoricoRequest(
    String estadoCivil,
    String ocupacao,
    String alergias,
    String medicamentos,
    String suplementacao,
    String historicoFamiliar,
    String outrasCondicoes,
    boolean isBebe,
    boolean isFuma
) {
}
