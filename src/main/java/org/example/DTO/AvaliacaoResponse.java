package org.example.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.enums.ProfissionalTipo;
import org.example.model.Avaliacao;
import org.example.jackson.core.JsonProcessingException;
import org.example.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Não retorna campos nulos
public class AvaliacaoResponse {

    private Long id;
    private LocalDate dataAvaliacao;
    private String nomePaciente;
    private Long pacienteId;
    private ProfissionalTipo tipoProfissional;
    
    private Object dados; 

    public static AvaliacaoResponse fromEntity(Avaliacao avaliacao, ObjectMapper mapper) {
        AvaliacaoResponse response = new AvaliacaoResponse();
        response.setId(avaliacao.getId());
        response.setDataAvaliacao(avaliacao.getDataAvaliacao());
        response.setTipoProfissional(avaliacao.getTipoProfissional());
        
        if (avaliacao.getPaciente() != null) {
            response.setNomePaciente(avaliacao.getPaciente().getNome());
            response.setPacienteId(avaliacao.getPaciente().getId());
        }

        try {
            if (avaliacao.getDadosEspecificos() != null && !avaliacao.getDadosEspecificos().isEmpty()) {
                Map<String, Object> dadosMap = mapper.readValue(avaliacao.getDadosEspecificos(), Map.class);
                response.setDados(dadosMap);
            }
        } catch (JsonProcessingException e) {
            response.setDados(avaliacao.getDadosEspecificos()); 
        }

        return response;
    }
}