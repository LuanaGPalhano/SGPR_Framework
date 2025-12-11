package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List; // <<< IMPORT NECESSÁRIO
import java.util.ArrayList; // <<< IMPORT NECESSÁRIO
import java.util.Set;

@Entity
@Table(name = "refeicoes")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refeicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalTime horario;
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dieta_id")
    private Dieta dieta;

    // --- CAMPO FALTANTE ADICIONADO AQUI ---
    @OneToMany(
            mappedBy = "refeicao",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<ItemRefeicao> itensRefeicao = new HashSet<>(); // Inicialize a lista

    @OneToMany(mappedBy = "refeicao", cascade = CascadeType.ALL)
    private java.util.List<ItemRefeicao> itens;

    public Refeicao(String nome, LocalTime horario, String descricao, Dieta dieta) {
        this.nome = nome;
        this.horario = horario;
        this.descricao = descricao;
        this.dieta = dieta;
    }

}
