package org.example.DTO;

import java.time.LocalTime;
import java.util.List;

// Verifique se o seu record RefeicaoResponse está assim:
public record RefeicaoResponse(
        Long id,
        String nome,
        LocalTime horario,
        String descricao,
        List<ItemRefeicaoResponse> itens // O 5º argumento deve ser uma Lista de ItemRefeicaoResponse
) {
}