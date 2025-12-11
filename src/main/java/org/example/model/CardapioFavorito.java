package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "cardapio_favorito")
@Data
@NoArgsConstructor
public class CardapioFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "refeicao_nome", nullable = false, length = 100)
    private String refeicaoNome; // Nome da refeição (ex: Jantar)

    @Column(name = "sugestao_texto", nullable = false, columnDefinition = "TEXT")
    private String sugestaoTexto; // O texto da sugestão favoritada

    @Column(name = "data_criacao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public CardapioFavorito(Paciente paciente, String refeicaoNome, String sugestaoTexto) {
        this.paciente = paciente;
        this.refeicaoNome = refeicaoNome;
        this.sugestaoTexto = sugestaoTexto;
    }
}