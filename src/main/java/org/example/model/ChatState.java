package org.example.model;

import lombok.Data;

@Data // Gera Getters e Setters automáticos (Lombok)
public class ChatState {

    // Onde o usuário está? (INICIO, ESCOLHER_OPCAO, AGUARDANDO_FAVORITO...)
    private String step = "INICIO";

    // Sobre o que ele está falando? (NUTRICAO, TREINO, ROTINA)
    private String topico = null;

    // Qual item ele escolheu? (Ex: ID da Dieta 50)
    private Long selecionadoId;

    // Qual detalhe ele pediu? (Ex: "Café da Manhã" ou "Execução")
    private String subOpcao;

    // O texto que a IA gerou (Para salvar nos favoritos depois)
    private String ultimaRespostaIA;
}