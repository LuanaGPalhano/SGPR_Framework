package org.example.service;

import org.example.DTO.CardapioFavoritoDTO;
import org.example.exception.ErrorResponse;
import org.example.integration.OpenAIIntegrationService; // Import do pacote correto
import org.example.model.CardapioFavorito;
import org.example.model.Dieta;
import org.example.model.ItemRefeicao;
import org.example.model.Paciente;
import org.example.model.Refeicao;
import org.example.repository.CardapioFavoritoRepository;
import org.example.repository.DietaRepository;
import org.example.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException; // Necessário para handleUserMessage
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócio do chat nutricional,
 * gerenciando o fluxo da conversa, interagindo com repositórios
 * e delegando a geração de texto para o OpenAIIntegrationService.
 */
@Service
public class ChatService {

    // --- Dependências ---
    private final DietaRepository dietaRepository;
    private final CardapioFavoritoRepository favoritoRepository;
    private final PacienteRepository pacienteRepository;
    private final OpenAIIntegrationService openAIIntegrationService; // Serviço de integração

    // --- Estado da Conversa (em memória) ---
    private final Map<Long, String> conversationState = new HashMap<>();
    private final Map<Long, Dieta> dietaSelecionada = new HashMap<>();
    private final Map<Long, Refeicao> refeicaoSelecionada = new HashMap<>();
    private final Map<Long, String> ultimaRespostaGerada = new HashMap<>();

    // --- Construtor ---
    @Autowired
    public ChatService(DietaRepository dietaRepository,
                       CardapioFavoritoRepository favoritoRepository,
                       PacienteRepository pacienteRepository,
                       OpenAIIntegrationService openAIIntegrationService) {
        this.dietaRepository = dietaRepository;
        this.favoritoRepository = favoritoRepository;
        this.pacienteRepository = pacienteRepository;
        this.openAIIntegrationService = openAIIntegrationService;
    }

    // --- Ponto de Entrada Principal ---
    /**
     * Processa a mensagem do usuário com base no estado atual da conversa.
     * @param pacienteId ID do paciente.
     * @param userMessage Mensagem enviada pelo usuário.
     * @return A resposta do assistente.
     * @throws IOException Se ocorrer erro (embora a chamada direta à rede não esteja mais aqui).
     */
    public String handleUserMessage(Long pacienteId, String userMessage) throws IOException {
        // Reseta se for a mensagem inicial
        if ("__INITIAL_MESSAGE__".equals(userMessage)) {
            resetConversationState(pacienteId);
        }

        String state = conversationState.getOrDefault(pacienteId, "INICIO");

        // Permite cancelar a qualquer momento
        if (userMessage.equalsIgnoreCase("sair") || userMessage.equalsIgnoreCase("cancelar")) {
            resetConversationState(pacienteId);
            return "Ok, cancelando a operação atual. Como posso ajudar?";
        }

        // Máquina de estados da conversa
        switch (state) {
            case "INICIO":
                return iniciarConversa(pacienteId);
            case "ESCOLHER_DIETA":
                return escolherDietaPorNumero(pacienteId, userMessage);
            case "CONFIRMAR_CARDAPIO":
                return confirmarCardapio(pacienteId, userMessage);
            case "ESCOLHER_REFEICAO":
                return escolherRefeicaoEGerarCardapio(pacienteId, userMessage);
            case "AGUARDANDO_FAVORITAR":
                return handleConfirmarFavorito(pacienteId, userMessage);
            case "ESCOLHENDO_FAVORITO":
                return handleSalvarFavorito(pacienteId, userMessage);
            default:
                resetConversationState(pacienteId);
                return "Não entendi sua resposta. Vamos começar de novo. " + iniciarConversa(pacienteId);
        }
    }

    // --- Métodos de Gerenciamento de Estado ---
    private void resetConversationState(Long pacienteId) {
        conversationState.put(pacienteId, "INICIO");
        dietaSelecionada.remove(pacienteId);
        refeicaoSelecionada.remove(pacienteId);
        ultimaRespostaGerada.remove(pacienteId);
    }

    // --- Etapas do Fluxo da Conversa ---

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
                resetConversationState(pacienteId);
                return "Ocorreu um erro, não encontrei a dieta selecionada. Vamos começar de novo. " + iniciarConversa(pacienteId);
            }
            conversationState.put(pacienteId, "ESCOLHER_REFEICAO");
            String refeicoesDisponiveis = dieta.getRefeicoes().stream()
                    .map(Refeicao::getNome)
                    .filter(Objects::nonNull) // Garante que não há nomes nulos
                    .collect(Collectors.joining(", "));
            return "Perfeito! Para qual refeição você deseja um cardápio?<br>(Refeições Cadastradas: " + refeicoesDisponiveis + ")";
        } else {
            resetConversationState(pacienteId);
            return "Entendido! Se mudar de ideia, é só me chamar.";
        }
    }

    private String escolherRefeicaoEGerarCardapio(Long pacienteId, String userMessage) {
        Dieta dieta = dietaSelecionada.get(pacienteId);
        // Verifica se a dieta ainda está selecionada (segurança)
        if (dieta == null) {
            resetConversationState(pacienteId);
            return "Parece que a seleção da dieta foi perdida. Vamos começar de novo. " + iniciarConversa(pacienteId);
        }

        final String userInput = userMessage.trim();
        Optional<Refeicao> refeicaoOpt = dieta.getRefeicoes().stream()
                .filter(r -> r.getNome() != null && r.getNome().trim().equalsIgnoreCase(userInput)) // Comparação segura
                .findFirst();

        if (refeicaoOpt.isEmpty()) {
            String refeicoesDisponiveis = dieta.getRefeicoes().stream()
                    .map(Refeicao::getNome)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", "));
            // Permite tentar de novo sem resetar o estado
            return "Não encontrei a refeição '" + userInput + "'. Por favor, escolha uma das opções válidas: " + refeicoesDisponiveis;
        }

        Refeicao refeicao = refeicaoOpt.get();
        refeicaoSelecionada.put(pacienteId, refeicao);

        // Monta os dados para enviar à IA
        String dadosRefeicao = construirDadosRefeicao(dieta, refeicao);

        // Delega a chamada à IA para o serviço de integração
        String respostaIA = openAIIntegrationService.gerarCardapioComIA(dadosRefeicao);

        // Salva a resposta e avança para o estado de perguntar sobre favoritar
        ultimaRespostaGerada.put(pacienteId, respostaIA);
        conversationState.put(pacienteId, "AGUARDANDO_FAVORITAR");

        // Retorna a resposta da IA + a pergunta sobre favoritar
        return respostaIA + "<br><br>Gostou de alguma sugestão? Deseja salvar alguma como favorita? (sim/não)";
    }

    // --- Fluxo de Favoritar ---

    private String handleConfirmarFavorito(Long pacienteId, String userMessage) {
        if (userMessage.equalsIgnoreCase("sim")) {
            conversationState.put(pacienteId, "ESCOLHENDO_FAVORITO");
            String dias = extrairDiasDaSemana(ultimaRespostaGerada.get(pacienteId));
            if (!dias.isEmpty()) {
                return "Legal! Qual dia da semana você gostaria de salvar? (" + dias + ")";
            } else {
                // Fallback se não encontrar dias na resposta da IA
                return "Legal! Qual parte do cardápio você gostaria de salvar? (Por favor, digite o dia da semana ou a primeira frase da sugestão)";
            }
        } else {
            resetConversationState(pacienteId); // Volta ao início se não quiser favoritar
            return "Ok! Se precisar de mais alguma coisa, é só perguntar.";
        }
    }

    @Transactional
    protected String handleSalvarFavorito(Long pacienteId, String userChoice) {
        String ultimaResposta = ultimaRespostaGerada.get(pacienteId);
        Refeicao refeicao = refeicaoSelecionada.get(pacienteId);
        // Dieta dieta = dietaSelecionada.get(pacienteId); // Não precisamos mais da dieta aqui
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);

        // Verificação de Nulos (sem dieta)
        if (ultimaResposta == null || refeicao == null || paciente == null) {
            resetConversationState(pacienteId);
            return "Ocorreu um erro ao recuperar as informações da conversa. Vamos começar de novo. " + iniciarConversa(pacienteId);
        }

        // Extrai o bloco de texto do dia escolhido
        String sugestaoParaSalvar = extrairSugestaoPorDia(ultimaResposta, userChoice.trim());

        // Verifica se a extração falhou
        if (sugestaoParaSalvar.startsWith("Não foi possível encontrar")) {
            String dias = extrairDiasDaSemana(ultimaResposta);
            // Permite tentar de novo sem resetar
            return sugestaoParaSalvar + " Por favor, digite um dos dias listados: ("+ dias +") ou 'cancelar'.";
        }

        // Cria o objeto favorito (sem nome da dieta)
        CardapioFavorito favorito = new CardapioFavorito(
                paciente,
                refeicao.getNome(),
                sugestaoParaSalvar
        );
        favoritoRepository.save(favorito); // Salva no banco

        resetConversationState(pacienteId); // Reseta a conversa após sucesso
        return "Sugestão salva com sucesso nos seus favoritos!<br><br>Posso ajudar com mais alguma coisa?";
    }

    // --- Métodos Auxiliares ---

    /** Monta a string com os dados da refeição para enviar à IA. */
    private String construirDadosRefeicao(Dieta dieta, Refeicao refeicao) {
        String alimentosComDetalhes = Optional.ofNullable(refeicao.getItensRefeicao())
                // CORREÇÃO 1: Usar emptySet() se getItensRefeicao() retornar Set
                // Se retornar List, mantenha emptyList(). Verifique sua classe Refeicao.
                // Vou assumir que retorna Set, baseado no erro.
                .orElseGet(Collections::emptySet)
                .stream()
                .filter(Objects::nonNull)
                .map(item -> {
                    // CORREÇÃO 2: Tratar getQuantidade() como Double (wrapper) para checar nulidade
                    Double quantidade = item.getQuantidade(); // Pega a quantidade (assume Double)
                    String quantidadeFormatada;
                    if (quantidade == null) {
                        quantidadeFormatada = "N/A"; // Define como N/A se for nulo
                    } else if (quantidade % 1 == 0) {
                        quantidadeFormatada = String.format("%.0f", quantidade); // Formata inteiro sem ".0"
                    } else {
                        quantidadeFormatada = String.valueOf(quantidade); // Mantém decimal
                    }

                    return String.format("%s (%s %s)",
                            Optional.ofNullable(item.getAlimento()).orElse("N/A"),
                            quantidadeFormatada, // Usa a quantidade já tratada
                            Optional.ofNullable(item.getUnidadeMedida()).orElse("N/A"));
                })
                .collect(Collectors.joining(", "));

        // CORREÇÃO 3: Formatar o horário ANTES do orElse ou usar orElse(null) e tratar depois
        String horarioFormatado = Optional.ofNullable(refeicao.getHorario())
                .map(java.time.format.DateTimeFormatter.ofPattern("HH:mm")::format) // Formata para HH:mm
                .orElse("N/A"); // Usa "N/A" se o horário for nulo

        return String.format(
                "--- DADOS DA REFEIÇÃO ORIGINAL ---\n" +
                        "Dieta: %s\n" +
                        "Objetivo da Dieta: %s\n" +
                        "Refeição a ser preparada: %s (Horário: %s)\n" + // Usa horarioFormatado
                        "Alimentos e Quantidades Base: %s\n" +
                        "--- FIM DOS DADOS ---",
                Optional.ofNullable(dieta.getNome()).orElse("N/A"),
                Optional.ofNullable(dieta.getObjetivo()).orElse("N/A"),
                Optional.ofNullable(refeicao.getNome()).orElse("N/A"),
                horarioFormatado, // Usa a string formatada ou "N/A"
                alimentosComDetalhes
        );
    }

    /** Extrai os nomes dos dias da semana da resposta formatada da IA. */
    private String extrairDiasDaSemana(String textoCompleto) {
        if (textoCompleto == null) return "";
        // Regex busca por "Segunda-feira", "Terça-feira", etc. dentro de <strong>
        Pattern pattern = Pattern.compile("<strong>\\s*(Segunda-feira|Terça-feira|Quarta-feira|Quinta-feira|Sexta-feira|Sábado|Domingo):?\\s*</strong>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textoCompleto);
        List<String> diasEncontrados = new ArrayList<>();
        while (matcher.find()) {
            diasEncontrados.add(matcher.group(1).trim()); // Adiciona o NOME do dia
        }
        return String.join(", ", diasEncontrados); // Retorna "Segunda-feira, Terça-feira, ..."
    }

    private String extrairSugestaoPorDia(String textoCompleto, String diaEscolhido) {
        if (textoCompleto == null || diaEscolhido == null || diaEscolhido.isEmpty()) {
            return "Não foi possível encontrar a sugestão (dados inválidos).";
        }
        String diaPattern = Pattern.quote(diaEscolhido);
        Pattern pattern = Pattern.compile(
                "<strong>\\s*" + diaPattern + ":?\\s*</strong>(?:<br>|\\s)*(.*?)(?=<strong|<h3|\\z)",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(textoCompleto);

        if (matcher.find()) {
            String conteudo = matcher.group(1).trim();

            return "<strong>" + diaEscolhido + "</strong><br>" + conteudo;
        } else {
            return "Não foi possível encontrar a sugestão para '" + diaEscolhido + "'. Verifique se digitou o nome do dia exatamente como aparece no cardápio (ex: Segunda-feira, Terça-feira).";
        }
    }

    /** Busca todos os favoritos de um paciente, retornando DTOs. */
    public List<CardapioFavoritoDTO> buscarFavoritosPorPaciente(Long pacienteId) {
        List<CardapioFavorito> favoritos = favoritoRepository.findByPacienteId(pacienteId);
        // Mapeia para DTO (sem dietaNome)
        return favoritos.stream()
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
                .orElseThrow(() -> new ErrorResponse.ResourceNotFoundException("Cardápio favorito com ID " + favoritoId + " não encontrado."));

        if (!favorito.getPaciente().getId().equals(pacienteId)) {
            throw new ErrorResponse.UnauthorizedOperationException("Paciente não autorizado a excluir este favorito.");
        }
        favoritoRepository.delete(favorito);
    }
}