package org.example.DTO;

import java.time.LocalDateTime;
import java.util.List;
import org.example.model.EntradaDiario;
import org.example.model.DiarioBase;

public record DiarioBaseResponse (
    Long id,
    String texto,
    String imgURL,
    List<EntradaDiario> entradas,
    LocalDateTime registroHorario
) {
    public DiarioBaseResponse(DiarioBase diario){
        this(diario.getId(), diario.getTexto(), diario.getImgURL(), diario.getEntradasDiario(), diario.getRegistroHorario());
    }
}

