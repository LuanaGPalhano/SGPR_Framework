package org.example.strategy.chat;

import org.example.model.PlanoTerapeutico;
import org.example.repository.PlanoTerapeuticoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanoTerapeuticoChatStrategy implements ChatStrategy {

    private final PlanoTerapeuticoRepository planoTerapeuticoRepository;

    public PlanoTerapeuticoChatStrategy(PlanoTerapeuticoRepository planoTerapeuticoRepository) {
        this.planoTerapeuticoRepository = planoTerapeuticoRepository;
    }

    @Override
    public boolean supports(String topico) {
        // Aceita "ROTINA" ou "PSICOLOGIA"
        return "ROTINA".equalsIgnoreCase(topico) || "PSICOLOGIA".equalsIgnoreCase(topico);
    }

    @Override
    public String getNomeEntidade() {
        return "Plano Terapêutico";
    }

    @Override
    public String verificarDisponibilidade(Long pacienteId) {
        if (planoTerapeuticoRepository.findByPacienteId(pacienteId).isEmpty()) {
            return "Não encontrei planos terapêuticos ativos. Converse com seu Psicólogo.";
        }
        return null;
    }

    @Override
    public String listarOpcoes(Long pacienteId) {
        List<PlanoTerapeutico> planos = planoTerapeuticoRepository.findByPacienteId(pacienteId);
        StringBuilder sb = new StringBuilder("Planos de Acompanhamento:<br><br>");
        for (int i = 0; i < planos.size(); i++) {
            // Assume que PlanoTerapeutico tem 'tipoAbordagem' (ex: TCC, Psicanálise)
            sb.append("<strong>").append(i + 1).append(".</strong> ")
                    .append(planos.get(i).getTipoAbordagem()).append(" (Objetivo: ").append(planos.get(i).getObjetivo()).append(")<br>");
        }
        sb.append("<br>Selecione o plano desejado pelo número.");
        return sb.toString();
    }

    @Override
    public Long processarEscolha(Long pacienteId, String inputUsuario) {
        try {
            List<PlanoTerapeutico> planos = planoTerapeuticoRepository.findByPacienteId(pacienteId);
            int escolha = Integer.parseInt(inputUsuario.trim());
            if (escolha < 1 || escolha > planos.size()) return null;
            return planos.get(escolha - 1).getId();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String listarSubOpcoes(Long planoId) {
        // Opções focadas em bem-estar mental baseadas no plano
        return "Como posso te apoiar hoje dentro deste plano?<br><br>" +
                "Escolha uma opção:<br>" +
                "- <strong>Reflexão</strong> (Uma mensagem para o seu dia)<br>" +
                "- <strong>Ansiedade</strong> (Técnicas de respiração e controle)<br>" +
                "- <strong>Foco</strong> (Dicas para organização e rotina)";
    }

    @Override
    public String gerarPrompt(Long planoId, String subOpcao) {
        // 1. Busca o Plano no Banco (Define a variável 'plano')
        PlanoTerapeutico plano = planoTerapeuticoRepository.findById(planoId).orElseThrow();

        // 2. Extrai o contexto (Define a variável 'contexto')
        // Se estiver nulo, usamos uma string vazia para não quebrar
        String contexto = plano.getAnotacoesSessao() != null ? plano.getAnotacoesSessao() : "Sem anotações prévias.";

        // 3. Retorna o Prompt formatado
        return """
               Aja como um Psicólogo (%s).
               Contexto Clínico: %s.
               O paciente quer ajuda com: "%s".
               
               LAYOUT HTML OBRIGATÓRIO:
               <h3>🧠 Momento de Reflexão</h3>
               <p>Olá. Pensando no seu contexto, aqui vai uma orientação:</p>
               
               <ul>
                   <li><b>O que observar:</b> Ponto de atenção sobre o sentimento.</li>
                   <li><b>Ação Prática:</b> Um exercício rápido (ex: respiração, escrita).</li>
               </ul>
               
               <br>
               <b>💭 Frase para o dia:</b> "Insira uma frase curta e fortalecedora aqui."
               """.formatted(
                plano.getTipoAbordagem(), // %s 1 (Define a abordagem)
                contexto,                 // %s 2 (Define o contexto)
                subOpcao                  // %s 3 (O que o usuário pediu)
        );
    }
}