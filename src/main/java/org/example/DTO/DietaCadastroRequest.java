package org.example.DTO;

public record DietaCadastroRequest(String nome, String descricao, Long pacienteId, Long nutricionistaId) {

}
