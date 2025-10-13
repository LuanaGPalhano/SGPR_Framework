package org.example.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import org.example.model.Dieta;
import org.example.model.ItemRefeicao;
import org.example.model.Refeicao;
import org.example.repository.DietaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final DietaRepository dietaRepository;
    private final OpenAiService openAiService;

    private final Map<Long, String> conversationState = new HashMap<>();
    private final Map<Long, Dieta> dietaSelecionada = new HashMap<>();

    @Autowired
    public ChatService(DietaRepository dietaRepository, @Value("${openai.api.key}") String apiKey) {
        this.dietaRepository = dietaRepository;
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    public String handleUserMessage(Long pacienteId, String userMessage) throws IOException {
        if ("__INITIAL_MESSAGE__".equals(userMessage)) {
            conversationState.put(pacienteId, "INICIO");
            dietaSelecionada.remove(pacienteId);
        }

        String state = conversationState.getOrDefault(pacienteId, "INICIO");

        switch (state) {
            case "INICIO":
                return iniciarConversa(pacienteId);
            case "ESCOLHER_DIETA":
                return escolherDietaPorNumero(pacienteId, userMessage);
            case "CONFIRMAR_CARDAPIO":
                return confirmarCardapio(pacienteId, userMessage);
            case "ESCOLHER_REFEICAO":
                return escolherRefeicao(pacienteId, userMessage);
            default:
                conversationState.put(pacienteId, "INICIO");
                return "Não entendi sua resposta. Vamos começar de novo. " + iniciarConversa(pacienteId);
        }
    }

    private String iniciarConversa(Long pacienteId) {
        List<Dieta> dietas = dietaRepository.findAllByPacienteIdWithDetails(pacienteId);
        if (dietas.isEmpty()) {
            return "Olá! Sou seu assistente nutricional, mas notei que você ainda não possui nenhuma dieta cadastrada.";
        }
        if (dietas.size() == 1) {
            Dieta dieta = dietas.get(0);
            dietaSelecionada.put(pacienteId, dieta);
            conversationState.put(pacienteId, "CONFIRMAR_CARDAPIO");
            return "Olá! Você possui a dieta '" + dieta.getNome() + "'. Deseja que eu gere um cardápio personalizado para alguma refeição? (Responda 'sim' ou 'não')";
        }
        conversationState.put(pacienteId, "ESCOLHER_DIETA");

        // CORREÇÃO 1: Usando <br> para quebra de linha no HTML
        StringBuilder sb = new StringBuilder("Olá! Você possui múltiplas dietas. Por favor, escolha uma delas digitando o número correspondente:<br><br>");
        for (int i = 0; i < dietas.size(); i++) {
            Dieta d = dietas.get(i);
            sb.append(i + 1).append(". ").append(d.getNome()).append(" (Objetivo: ").append(d.getObjetivo()).append(")<br>");
        }
        return sb.toString();
    }

    private String escolherDietaPorNumero(Long pacienteId, String userMessage) {
        List<Dieta> dietas = dietaRepository.findAllByPacienteIdWithDetails(pacienteId);
        try {
            int escolha = Integer.parseInt(userMessage.trim());
            if (escolha < 1 || escolha > dietas.size()) {
                return "Número inválido. Por favor, escolha um número entre 1 e " + dietas.size() + ".";
            }
            Dieta dieta = dietas.get(escolha - 1);
            dietaSelecionada.put(pacienteId, dieta);
            conversationState.put(pacienteId, "CONFIRMAR_CARDAPIO");
            return "Ótimo, você escolheu a dieta '" + dieta.getNome() + "'. Deseja que eu gere um cardápio personalizado para alguma refeição? (Responda 'sim' ou 'não')";
        } catch (NumberFormatException e) {
            return "Entrada inválida. Por favor, digite apenas o número da dieta desejada.";
        }
    }

    private String confirmarCardapio(Long pacienteId, String userMessage) {
        if (userMessage.equalsIgnoreCase("sim")) {
            Dieta dieta = dietaSelecionada.get(pacienteId);
            if (dieta == null) {
                conversationState.put(pacienteId, "INICIO");
                return "Ocorreu um erro, não encontrei a dieta selecionada. Vamos começar de novo. " + iniciarConversa(pacienteId);
            }
            conversationState.put(pacienteId, "ESCOLHER_REFEICAO");
            String refeicoesDisponiveis = dieta.getRefeicoes().stream().map(Refeicao::getNome).collect(Collectors.joining(", "));

            // CORREÇÃO 2: Usando <br> para quebra de linha no HTML
            return "Perfeito! Para qual refeição você deseja um cardápio?<br>(Refeições Cadastradas: " + refeicoesDisponiveis + ")";
        } else {
            conversationState.put(pacienteId, "INICIO");
            dietaSelecionada.remove(pacienteId);
            return "Entendido! Se mudar de ideia, é só me chamar.";
        }
    }

    private String escolherRefeicao(Long pacienteId, String userMessage) {
        Dieta dieta = dietaSelecionada.get(pacienteId);

        // MUDANÇA 1: Tornando a busca pela refeição mais robusta
        final String userInput = userMessage.trim();
        Optional<Refeicao> refeicaoOpt = dieta.getRefeicoes().stream()
                .filter(r -> r.getNome().trim().equalsIgnoreCase(userInput))
                .findFirst();

        if (refeicaoOpt.isEmpty()) {
            String refeicoesDisponiveis = dieta.getRefeicoes().stream().map(Refeicao::getNome).collect(Collectors.joining(", "));
            return "Não encontrei a refeição '" + userInput + "'. Por favor, escolha uma das opções válidas: " + refeicoesDisponiveis;
        }

        Refeicao refeicao = refeicaoOpt.get();
        String prompt = construirPromptFinal(dieta, refeicao);
        String resposta = chamarModeloOpenAI(prompt);
        conversationState.put(pacienteId, "INICIO");
        dietaSelecionada.remove(pacienteId);
        return resposta;
    }

    private String construirPromptFinal(Dieta dieta, Refeicao refeicao) {
        // MUDANÇA 2: Ajustando a formatação dos números
        String alimentosComDetalhes = refeicao.getItensRefeicao().stream()
                .map(item -> {
                    // Formata o número para não mostrar ".0" se for inteiro
                    String quantidadeFormatada = (item.getQuantidade() % 1 == 0)
                            ? String.format("%.0f", item.getQuantidade())
                            : String.valueOf(item.getQuantidade());
                    return String.format("%s (%s %s)", item.getAlimento(), quantidadeFormatada, item.getUnidadeMedida());
                })
                .collect(Collectors.joining(", "));

        // MUDANÇA 3: Adicionando um comando final explícito no prompt
        return "A seguir estão os dados de uma refeição específica. Sua tarefa é criar um cardápio semanal com base neles, seguindo as regras que você já conhece.\n\n" +
                "--- DADOS DA REFEIÇÃO ORIGINAL ---\n" +
                "Dieta: " + dieta.getNome() + "\n" +
                "Objetivo da Dieta: " + dieta.getObjetivo() + "\n" +
                "Refeição a ser preparada: " + refeicao.getNome() + " (Horário: " + refeicao.getHorario() + ")\n" +
                "Alimentos e Quantidades Base: " + alimentosComDetalhes + "\n" +
                "--- FIM DOS DADOS ---\n\n" +
                "Agora, gere o cardápio semanal completo.";
    }

    private String chamarModeloOpenAI(String dadosDaRefeicao) {
        try {
            // MUDANÇA FINAL: Removida a regra de recusa para eliminar a confusão da IA.
            String instrucaoSistema = "Você é um nutricionista virtual criativo e especialista em formatação HTML. Sua tarefa é criar um cardápio semanal (7 dias) variado para a refeição especificada nos dados do usuário.\n\n" +
                    "REGRAS OBRIGATÓRIAS:\n" +
                    "1. O cardápio deve se basear estritamente nos alimentos e quantidades originais da dieta do usuário.\n" +
                    "2. Ao sugerir um alimento base (ex: frango, arroz), você DEVE incluir a sua quantidade original. Exemplo: 'Peito de frango grelhado (120g)'.\n\n" +
                    "REGRAS DE FORMATAÇÃO HTML:\n" +
                    "1. Use <br> para quebras de linha. Use <br><br> para parágrafos.\n" +
                    "2. Use <strong> para títulos de dias da semana.\n" +
                    "3. Comece com uma introdução curta e amigável.\n" +
                    "4. Use um título principal como '<h3>🌙 Cardápio para o [Nome da Refeição]</h3>'.\n" +
                    "5. Liste os 7 dias, cada um com suas sugestões.\n" +
                    "6. Termine com um título '<h3>🥗 Dicas personalizadas:</h3>' e 3 dicas curtas.";

            ChatMessage systemMessage = new ChatMessage("system", instrucaoSistema);
            ChatMessage userMessage = new ChatMessage("user", dadosDaRefeicao);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-4o-mini")
                    .messages(Arrays.asList(systemMessage, userMessage))
                    .maxTokens(1500)
                    .temperature(0.7)
                    .build();

            return openAiService.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();

        } catch (Exception e) {
            e.printStackTrace();
            return "Desculpe, não consegui me comunicar com o assistente da OpenAI no momento.";
        }
    }
}