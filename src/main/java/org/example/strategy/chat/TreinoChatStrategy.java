package org.example.strategy.chat;

import org.example.model.Treino;
import org.example.repository.TreinoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TreinoChatStrategy implements ChatStrategy {

    private final TreinoRepository treinoRepository;

    public TreinoChatStrategy(TreinoRepository treinoRepository) {
        this.treinoRepository = treinoRepository;
    }

    @Override
    public boolean supports(String topico) {
        return "TREINO".equalsIgnoreCase(topico);
    }

    @Override
    public String getNomeEntidade() {
        return "Treino";
    }

    @Override
    public String verificarDisponibilidade(Long pacienteId) {
        if (treinoRepository.findByPacienteId(pacienteId).isEmpty()) {
            return "Você ainda não possui treinos cadastrados pelo seu Educador Físico.";
        }
        return null;
    }

    @Override
    public String listarOpcoes(Long pacienteId) {
        List<Treino> treinos = treinoRepository.findByPacienteId(pacienteId);
        StringBuilder sb = new StringBuilder("Seus treinos atuais:<br><br>");
        for (int i = 0; i < treinos.size(); i++) {
            // Assume que Treino tem um campo 'tipoTreino' (ex: Hipertrofia A, Cardio)
            sb.append("<strong>").append(i + 1).append(".</strong> ")
                    .append(treinos.get(i).getTipoTreino()).append("<br>");
        }
        sb.append("<br>Qual treino você quer analisar? Digite o número.");
        return sb.toString();
    }

    @Override
    public Long processarEscolha(Long pacienteId, String inputUsuario) {
        try {
            List<Treino> treinos = treinoRepository.findByPacienteId(pacienteId);
            int escolha = Integer.parseInt(inputUsuario.trim());
            if (escolha < 1 || escolha > treinos.size()) return null;
            return treinos.get(escolha - 1).getId();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String listarSubOpcoes(Long treinoId) {
        // Diferente da dieta, aqui oferecemos TIPOS DE AJUDA sobre o treino selecionado
        return "O que você gostaria de saber sobre este treino?<br><br>" +
                "Digite uma das opções abaixo:<br>" +
                "- <strong>Execução</strong> (Dicas de como fazer os exercícios)<br>" +
                "- <strong>Carga</strong> (Como progredir o peso)<br>" +
                "- <strong>Adaptação</strong> (Opções para fazer em casa)";
    }

    @Override
    public String gerarPrompt(Long treinoId, String subOpcao) {
        Treino treino = treinoRepository.findById(treinoId).orElseThrow();
        String exercicios = treino.getDescricaoExercicios();

        return """
               Aja como um Personal Trainer experiente e motivador.
               
               CONTEXTO:
               O aluno tem este treino: [%s]
               O objetivo é: %s
               
               O aluno quer saber sobre: "%s"
               
               SUA TAREFA:
               Crie uma resposta estruturada para cada exercício relevante da lista acima.
               Siga estritamente este modelo de layout HTML para cada exercício:
               
               <h3>Nome do Exercício</h3>
               <ul>
                   <li><b>Execução:</b> Explique o movimento passo a passo.</li>
                   <li><b>🔧 Dicas Técnicas:</b> Liste 2 ou 3 erros comuns ou ajustes de postura.</li>
               </ul>
               <b>💡 Motivação:</b> Uma frase curta sobre o benefício deste exercício.
               <hr>
               
               Finalize com uma mensagem geral de incentivo.
               """.formatted(exercicios, treino.getObjetivo(), subOpcao);
    }
}