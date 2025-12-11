package org.example.DTO;

import lombok.Data;
import java.util.Map;

@Data
public class AvaliacaoRequest {
    // Um mapa flexível para receber qualquer campo do front-end
    private Map<String, Object> dados;

    // Métodos auxiliares para converter tipos com segurança
    public Double getDouble(String key) {
        if (dados.get(key) == null) return 0.0;
        return Double.parseDouble(dados.get(key).toString());
    }

    public Integer getInt(String key) {
        if (dados.get(key) == null) return 0;
        return Integer.parseInt(dados.get(key).toString());
    }

    public String getString(String key) {
        return (String) dados.getOrDefault(key, "");
    }
}