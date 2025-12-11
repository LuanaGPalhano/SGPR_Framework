package org.example.strategy.chat;

import org.example.model.Dieta;
import org.example.model.Refeicao;
import org.example.repository.DietaRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NutricaoChatStrategy implements ChatStrategy {

    private final DietaRepository dietaRepository;

    public NutricaoChatStrategy(DietaRepository dietaRepository) {
        this.dietaRepository = dietaRepository;
    }

    @Override
    public boolean supports(String topico) {
        // Aceita "NUTRICAO" ou "DIETA"
        return "NUTRICAO".equalsIgnoreCase(topico) || "DIETA".equalsIgnoreCase(topico);
    }

    @Override
    public String getNomeEntidade() {
        return "Dieta";
    }

    @Override
    public String verificarDisponibilidade(Long pacienteId) {
        // Usa o método que definimos na interface do repositório
        if (dietaRepository.findAllByPacienteIdWithDetails(pacienteId).isEmpty()) {
            return "Não encontrei dietas cadastradas para você. Peça ao seu Nutricionista para criar uma.";
        }
        return null;
    }

    @Override
    public String listarOpcoes(Long pacienteId) {
        List<Dieta> dietas = dietaRepository.findAllByPacienteIdWithDetails(pacienteId);
        StringBuilder sb = new StringBuilder("Encontrei as seguintes dietas:<br><br>");
        for (int i = 0; i < dietas.size(); i++) {
            Dieta d = dietas.get(i);
            sb.append("<strong>").append(i + 1).append(".</strong> ")
                    .append(d.getNome()).append(" (Objetivo: ").append(d.getObjetivo()).append(")<br>");
        }
        sb.append("<br>Digite o número da dieta que deseja consultar.");
        return sb.toString();
    }

    @Override
    public Long processarEscolha(Long pacienteId, String inputUsuario) {
        try {
            List<Dieta> dietas = dietaRepository.findAllByPacienteIdWithDetails(pacienteId);
            int escolha = Integer.parseInt(inputUsuario.trim());
            if (escolha < 1 || escolha > dietas.size()) return null;
            return dietas.get(escolha - 1).getId();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String listarSubOpcoes(Long dietaId) {
        // Busca a dieta completa (ideal usar o método com FETCH se possível para performance)
        Dieta dieta = dietaRepository.findById(dietaId).orElseThrow();

        String refeicoes = dieta.getRefeicoes().stream()
                .map(Refeicao::getNome)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));

        return "Você selecionou a dieta: <strong>" + dieta.getNome() + "</strong>.<br>" +
                "Para qual refeição você quer sugestões de cardápio?<br><br>" +
                "Opções disponíveis: " + refeicoes;
    }

    @Override
    public String gerarPrompt(Long dietaId, String nomeRefeicao) {
        // 1. Busca a Dieta no Banco (Definição da variável 'dieta')
        Dieta dieta = dietaRepository.findById(dietaId).orElseThrow();

        // 2. Busca a Refeição dentro da Dieta (Definição da variável 'refeicao')
        Refeicao refeicao = dieta.getRefeicoes().stream()
                .filter(r -> r.getNome().trim().equalsIgnoreCase(nomeRefeicao.trim()))
                .findFirst()
                .orElse(null);

        if (refeicao == null) {
            return "Erro: A refeição '" + nomeRefeicao + "' não foi encontrada nesta dieta.";
        }

        // 3. Formata a lista de alimentos (Definição da variável 'alimentos')
        String alimentos = Optional.ofNullable(refeicao.getItensRefeicao())
                .orElseGet(Collections::emptySet) // Use emptyList() se sua model usar List
                .stream()
                .map(item -> "<li>" + item.getAlimento() + " (" + item.getQuantidade() + " " + item.getUnidadeMedida() + ")</li>")
                .collect(Collectors.joining(""));

        // 4. Retorna o Prompt usando as variáveis definidas acima
        return """
               Aja como um Nutricionista Funcional e Gastronômico.
               
               DADOS:
               - Refeição: %s (Horário: %s)
               - Objetivo da Dieta: %s
               
               ALIMENTOS BASE (Respeite esta lista):
               <ul>
               %s
               </ul>
               
               TAREFA:
               Gere uma sugestão de preparo criativa para esta refeição.
               
               LAYOUT HTML OBRIGATÓRIO:
               <h3>🍽️ Sugestão para %s</h3>
               <p>Aqui está uma forma deliciosa de preparar seus alimentos:</p>
               
               <ul>
                   <li><b>Ingrediente Principal:</b> Sugestão de preparo.</li>
                   <li><b>Acompanhamentos:</b> Como combinar.</li>
               </ul>
               
               <b>🌿 Toque do Chef:</b> Sugira um tempero natural.
               <br><br>
               <i>Por que isso ajuda no objetivo?</i> Explique em 1 frase.
               """.formatted(
                refeicao.getNome(),      // %s 1
                refeicao.getHorario(),   // %s 2
                dieta.getObjetivo(),     // %s 3
                alimentos,               // %s 4
                refeicao.getNome()       // %s 5
        );
    }
}