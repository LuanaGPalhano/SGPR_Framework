package org.example.DTO;

import lombok.*;
import java.util.List;

import org.example.model.Refeicoes;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiarioAlimentarRequest {
    private String texto;
    private String imgURL;
    private List<Refeicoes> refeicoes;
}
