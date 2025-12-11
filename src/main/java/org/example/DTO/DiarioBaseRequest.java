package org.example.DTO;

import java.util.List;

public record DiarioBaseRequest(
    String tipo,
    String texto,
    String imgURL,
    String pacienteCpf,
    List<EntradaDiarioRequest> entradas
) {
}