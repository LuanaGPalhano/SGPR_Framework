package org.example.DTO;

import java.time.LocalTime;
import java.util.List;

public record RefeicaoRequest(
        String nome,
        LocalTime horario,
        String descricao,
        List<ItemRefeicaoRequest> itens
) {}