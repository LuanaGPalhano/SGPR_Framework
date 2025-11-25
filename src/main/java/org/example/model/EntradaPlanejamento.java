package org.example.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "planejamento_entrada")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EntradaPlanejamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dia;
    private String refeicao;

    @ManyToOne
    @JoinColumn(name = "planejamento_id")
    private Planejamento planejamento;

    @OneToMany(mappedBy = "entrada", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemRefeicao> itensRefeicao = new ArrayList<>();
}
