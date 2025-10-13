package org.example.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import org.example.model.Refeicoes;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiarioAlimentarResponse {
    private Long id;
    private String texto;
    private String imgURL;
    private List<Refeicoes> refeicoes;
    private LocalDateTime registroHorario;
}

