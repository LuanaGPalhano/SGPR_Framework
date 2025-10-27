package org.example.integration;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService; // Import da biblioteca
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Service
public class OpenAIIntegrationService {

    private final OpenAiService openAiService; // Instância do serviço da biblioteca OpenAI

    // Construtor: Recebe a chave da API do application.properties e inicializa o serviço
    public OpenAIIntegrationService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60)); // Timeout de 60s
    }
    public String gerarCardapioComIA(String dadosDaRefeicao) {
        try {
            // A instrução do sistema agora reside DENTRO deste serviço especializado
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
                    .model("gpt-4o-mini") // Modelo da OpenAI
                    .messages(Arrays.asList(systemMessage, userMessage))
                    .maxTokens(1500)
                    .temperature(0.7)
                    .build();

            // Chama a API da OpenAI usando o serviço da biblioteca
            return openAiService.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();

        } catch (Exception e) {
            e.printStackTrace();
            return "Desculpe, não consegui gerar o cardápio no momento devido a um problema com o assistente de IA.";
        }
    }
}