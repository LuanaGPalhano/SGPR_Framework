package org.example.DTO;

import java.util.List;

import org.example.model.EntradaDiario;

public record DiarioAlimentarRequest(
    String texto,
    String imgURL,
    List<EntradaDiario> entradas
) {
}