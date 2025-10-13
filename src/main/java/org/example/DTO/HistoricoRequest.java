package org.example.DTO;

public record  HistoricoRequest(
    String estadoCiviil,
    String ocupacao,
    String alergias,
    String medicamentos,
    String suplementacao,
    String historicoFamiliar,
    String outrasCondicoes,
    boolean bebe,
    boolean fuma,
    Long pacienteId,
    Long avaliacaoId
) {
}
