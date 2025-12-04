package org.example.service;

import org.example.DTO.CardapioFavoritoDTO;
import org.example.exception.ErrorResponse;
import org.example.integration.OpenAIIntegrationService;
import org.example.model.CardapioFavorito;
import org.example.model.Paciente;
import org.example.repository.CardapioFavoritoRepository;
import org.example.repository.PacienteRepository;
import org.example.service.chat.ChatState;
import org.example.strategy.chat.ChatStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    // --- Dependências ---
    private final List<ChatStrategy> strategies; // Lista de todas as estratégias (Nutri, Treino, Rotina)
    private final OpenAIIntegrationService openAIIntegrationService;
    private final CardapioFavoritoRepository favoritoRepository;
    private final PacienteRepository pacienteRepository;

    // --- Gerenciamento de Estado (Memória) ---
    private final Map<Long, ChatState> userStates = new HashMap<>();

    @Autowired
    public ChatService(List<ChatStrategy> strategies,
                       OpenAIIntegrationService openAIIntegrationService,
                       CardapioFavoritoRepository favoritoRepository,
                       PacienteRepository pacienteRepository) {
        this.strategies = strategies;
        this.openAIIntegrationService = openAIIntegrationService;
        this.favoritoRepository = favoritoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Ponto de Entrada Principal.
     * Gerencia a máquina de estados da conversa.
     */
    public String handleUserMessage(Long pacienteId, String userMessage) throws IOException {

        // 1. Reset ou Início
        if ("__INITIAL_MESSAGE__".equals(userMessage) ||
                userMessage.equalsIgnoreCase("sair") ||
                userMessage.equalsIgnoreCase("cancelar")) {
            userStates.remove(pacienteId);
            return "Olá! Sou seu assistente de saúde. Sobre qual área deseja falar hoje?<br><br>" +
                    "Digite: <strong>Nutricao</strong>, <strong>Treino</strong> ou <strong>Rotina</strong>.";
        }

        // Recupera ou cria o estado do usuário
        ChatState state = userStates.computeIfAbsent(pacienteId, k -> new ChatState());

        // 2. Definição do Tópico (Se ainda não foi escolhido)
        if (state.getTopico() == null) {
            return definirTopico(pacienteId, state, userMessage);
        }

        // 3. Execução da Estratégia Selecionada
        ChatStrategy strategy = getStrategy(state.getTopico());

        switch (state.getStep()) {
            case "ESCOLHER_OPCAO":
                return processarEscolhaOpcao(pacienteId, state, strategy, userMessage);

            case "ESCOLHER_SUB_OPCAO":
                return processarSubOpcaoEChamarIA(state, strategy, userMessage);

            case "AGUARDANDO_FAVORITAR":
                return processarFavorito(pacienteId, state, userMessage);

            default:
                userStates.remove(pacienteId);
                return "Ocorreu um erro inesperado. Vamos recomeçar.";
        }
    }

    // --- Lógica de Fluxo (Private Methods) ---

    private String definirTopico(Long pacienteId, ChatState state, String userMessage) {
        String input = userMessage.toUpperCase();
        String topicoSelecionado = null;

        if (input.contains("NUTRI") || input.contains("DIETA")) topicoSelecionado = "NUTRICAO";
        else if (input.contains("TREINO")) topicoSelecionado = "TREINO";
        else if (input.contains("ROTINA") || input.contains("PSICO")) topicoSelecionado = "ROTINA";

        if (topicoSelecionado == null) {
            return "Não entendi. Por favor, escolha entre: <strong>Nutricao</strong>, <strong>Treino</strong> ou <strong>Rotina</strong>.";
        }

        // Verifica se a estratégia existe e se o paciente tem dados
        ChatStrategy strategy = getStrategy(topicoSelecionado);
        String erroDisponibilidade = strategy.verificarDisponibilidade(pacienteId);

        if (erroDisponibilidade != null) {
            return erroDisponibilidade + "<br>Escolha outra área ou cadastre dados com seu profissional.";
        }

        // Tudo certo, avança o estado
        state.setTopico(topicoSelecionado);
        state.setStep("ESCOLHER_OPCAO");

        return strategy.listarOpcoes(pacienteId);
    }

    private String processarEscolhaOpcao(Long pacienteId, ChatState state, ChatStrategy strategy, String userMessage) {
        try {
            Long idSelecionado = strategy.processarEscolha(pacienteId, userMessage);

            if (idSelecionado == null) {
                return "Opção inválida. Por favor, digite o número correspondente.";
            }

            state.setSelecionadoId(idSelecionado);
            state.setStep("ESCOLHER_SUB_OPCAO");

            return strategy.listarSubOpcoes(idSelecionado);

        } catch (Exception e) {
            return "Erro ao processar sua escolha. Tente digitar apenas o número.";
        }
    }

    private String processarSubOpcaoEChamarIA(ChatState state, ChatStrategy strategy, String userMessage) {
        state.setSubOpcao(userMessage);

        // O Service pede para a estratégia montar o texto correto
        String prompt = strategy.gerarPrompt(state.getSelecionadoId(), userMessage);

        if (prompt.startsWith("Erro:")) {
            return prompt + " Tente novamente.";
        }

        // Chama a IA (Ponto Fixo)
        String respostaIA = openAIIntegrationService.gerarRespostaComIA(prompt);
        state.setUltimaRespostaIA(respostaIA);

        state.setStep("AGUARDANDO_FAVORITAR");

        return respostaIA + "<br><br><strong>Gostou da sugestão? Deseja salvar nos favoritos? (Sim/Não)</strong>";
    }

    private String processarFavorito(Long pacienteId, ChatState state, String userMessage) {
        if (userMessage.equalsIgnoreCase("sim") || userMessage.equalsIgnoreCase("s")) {
            salvarFavorito(pacienteId, state);
            userStates.remove(pacienteId); // Encerra o fluxo
            return "Conteúdo salvo com sucesso! Posso ajudar em algo mais? (Digite Nutricao, Treino ou Rotina)";
        } else {
            userStates.remove(pacienteId); // Encerra o fluxo
            return "Entendido! Estou à disposição se precisar de mais alguma coisa.";
        }
    }

    // --- Métodos Auxiliares e Repositórios ---

    private ChatStrategy getStrategy(String topico) {
        return strategies.stream()
                .filter(s -> s.supports(topico))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estratégia não encontrada para o tópico: " + topico));
    }

    @Transactional
    protected void salvarFavorito(Long pacienteId, ChatState state) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ErrorResponse.ResourceNotFoundException("Paciente não encontrado"));

        // Adaptação: Usamos os campos existentes de CardapioFavorito para salvar Treinos/Rotinas também
        CardapioFavorito favorito = new CardapioFavorito();
        favorito.setPaciente(paciente);

        // Ex: "TREINO: Dicas de Execução" ou "NUTRICAO: Café da Manhã"
        String titulo = state.getTopico() + " - " + state.getSubOpcao();
        favorito.setRefeicaoNome(titulo);

        favorito.setSugestaoTexto(state.getUltimaRespostaIA());

        favoritoRepository.save(favorito);
    }

    // Mantém os métodos de Listagem e Exclusão que o Controller usa
    public List<CardapioFavoritoDTO> buscarFavoritosPorPaciente(Long pacienteId) {
        return favoritoRepository.findByPacienteId(pacienteId).stream()
                .map(fav -> new CardapioFavoritoDTO(
                        fav.getId(),
                        fav.getRefeicaoNome(),
                        fav.getSugestaoTexto(),
                        fav.getDataCriacao()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirFavorito(Long favoritoId, Long pacienteId) {
        CardapioFavorito favorito = favoritoRepository.findById(favoritoId)
                .orElseThrow(() -> new ErrorResponse.ResourceNotFoundException("Favorito não encontrado."));

        if (!favorito.getPaciente().getId().equals(pacienteId)) {
            throw new ErrorResponse.UnauthorizedOperationException("Não autorizado.");
        }
        favoritoRepository.delete(favorito);
    }
}