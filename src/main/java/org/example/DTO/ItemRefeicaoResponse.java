package org.example.DTO;

// Verifique se você também tem este record:
public record ItemRefeicaoResponse(
        Long id,
        String alimento,
        Double quantidade,
        String unidadeMedida,
        String resumoNutricional
) {
}