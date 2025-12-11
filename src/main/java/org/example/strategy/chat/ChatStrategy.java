package org.example.strategy.chat;

public interface ChatStrategy {

    // 1. Identificação: "Esse assunto é comigo?" (NUTRICAO, TREINO, ROTINA/PSICOLOGIA)
    boolean supports(String topico);

    // 2. Nome da Entidade para mensagens (Ex: "Dieta", "Treino", "Plano Terapêutico")
    String getNomeEntidade();

    // 3. Validação: O paciente tem dados cadastrados?
    String verificarDisponibilidade(Long pacienteId);

    // 4. Listagem Principal: Mostra Dietas, Treinos ou Planos
    String listarOpcoes(Long pacienteId);

    // 5. Processamento: Transforma a escolha "1" no ID do objeto (Dieta ID 10)
    Long processarEscolha(Long pacienteId, String inputUsuario);

    // 6. Detalhamento: Mostra Refeições, Dicas de Treino ou Opções Terapêuticas
    String listarSubOpcoes(Long idSelecionado);

    // 7. Ação: Gera o texto para mandar para a IA
    String gerarPrompt(Long idSelecionado, String subOpcao);
}