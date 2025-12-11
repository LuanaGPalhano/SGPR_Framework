package org.example.DTO;

public record ItemRefeicaoRequest(
        String alimento,
        double quantidade,
        String unidadeMedida,
        String resumoNutricional
) {}