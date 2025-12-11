package org.example.integration;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

@Service
public class OpenAIIntegrationService {

    private final OpenAiService openAiService;

    // Construtor: Inicializa o serviço com a API Key e Timeout
    public OpenAIIntegrationService(@Value("${openai.api.key}") String apiKey) {
        // Timeout de 60s para garantir que respostas longas não quebrem
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    /**
     * Método Genérico de Geração.
     * Recebe o prompt já montado pela Estratégia (Nutri/Treino/Rotina)
     * e garante que a IA responda no formato HTML correto para o Frontend.
     *
     * @param promptCompleto O texto contendo contexto, persona e pedido do usuário.
     * @return A resposta da IA formatada em HTML.
     */
    public String gerarRespostaComIA(String promptCompleto) {
        try {
            // --- INSTRUÇÃO DE SISTEMA (REGRAS GLOBAIS) ---
            // Aqui definimos QUEBRAR o Markdown e USAR HTML.
            // A "Persona" (Nutricionista, Personal) vem no 'promptCompleto', não aqui.
            String systemInstruction = """
                Você é o assistente inteligente do sistema SGPR.
                
                REGRAS DE FORMATAÇÃO ESTRITA (FRONTEND HTML):
                1. Sua resposta será renderizada diretamente em um navegador web.
                2. NÃO use formatação Markdown (como **, ##, -, ```). O frontend não lê Markdown.
                3. USE EXCLUSIVAMENTE tags HTML para formatar o texto:
                   - <b>texto</b> para negrito/destaque.
                   - <br> para pular linha (use <br><br> para parágrafos).
                   - <h3>Título</h3> para títulos de seções.
                   - <ul><li>Item 1</li><li>Item 2</li></ul> para listas.
                   - <i>texto</i> para itálico.
                   - <hr> para linhas separadoras.
                4. Use Emojis para tornar a leitura visual e agradável.
                5. Seja direto, organizado e profissional.
                
                Siga a persona e o contexto definidos na mensagem do usuário.
                """;

            // Monta as mensagens
            ChatMessage systemMessage = new ChatMessage("system", systemInstruction);
            ChatMessage userMessage = new ChatMessage("user", promptCompleto);

            // Configura a requisição
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model("gpt-4o-mini") // Modelo eficiente e rápido
                    .messages(Arrays.asList(systemMessage, userMessage))
                    .maxTokens(2000) // Limite de tamanho da resposta
                    .temperature(0.7) // Criatividade equilibrada
                    .build();

            // Envia e retorna o conteúdo
            return openAiService.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();

        } catch (Exception e) {
            e.printStackTrace();
            return "Desculpe, o assistente de IA está indisponível no momento. Por favor, tente novamente em alguns instantes. <br><small>Erro técnico: " + e.getMessage() + "</small>";
        }
    }
}