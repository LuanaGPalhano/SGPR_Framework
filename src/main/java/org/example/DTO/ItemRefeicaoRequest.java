package org.example.DTO;

import java.util.Map;

public record HistoricoRequest(
    // --- 1. PONTOS FIXOS (Comuns a todos os profissionais) ---
    String estadoCivil,
    String ocupacao,
    boolean bebe, 
    boolean fuma, 

    // --- 2. PONTOS VARIÁVEIS (Específicos da área) ---
    Map<String, Object> dados
) {

    public String getString(String key) {
        if (dados == null || !dados.containsKey(key)) {
            return "";
        }
        return String.valueOf(dados.get(key));
    }

    public boolean getBoolean(String key) {
        if (dados == null || !dados.containsKey(key)) {
            return false;
        }
        Object value = dados.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    public Integer getInt(String key) {
        if (dados == null || !dados.containsKey(key)) {
            return 0;
        }
        try {
            return Integer.parseInt(String.valueOf(dados.get(key)));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public Double getDouble(String key) {
        if (dados == null || !dados.containsKey(key)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(String.valueOf(dados.get(key)));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}