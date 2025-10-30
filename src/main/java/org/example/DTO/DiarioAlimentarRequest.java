package org.example.DTO;

import lombok.*;
import java.util.List;

import org.example.model.EntradaDiario;
import org.example.model.EntradaDiario;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiarioAlimentarRequest {
    private String texto;
    private String imgURL;
    private List<EntradaDiario> entradas;
}
