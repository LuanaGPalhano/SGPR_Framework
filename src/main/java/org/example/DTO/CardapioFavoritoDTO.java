package org.example.DTO;

import java.time.LocalDateTime;

public class CardapioFavoritoDTO {
    private Long id;
    private String refeicaoNome;
    private String sugestaoTexto;
    private LocalDateTime dataCriacao;

    public CardapioFavoritoDTO() {}

    public CardapioFavoritoDTO(Long id, String refeicaoNome, String sugestaoTexto, LocalDateTime dataCriacao) {
        this.id = id;
        this.refeicaoNome = refeicaoNome;
        this.sugestaoTexto = sugestaoTexto;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRefeicaoNome() { return refeicaoNome; }
    public void setRefeicaoNome(String refeicaoNome) { this.refeicaoNome = refeicaoNome; }
    public String getSugestaoTexto() { return sugestaoTexto; }
    public void setSugestaoTexto(String sugestaoTexto) { this.sugestaoTexto = sugestaoTexto; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}