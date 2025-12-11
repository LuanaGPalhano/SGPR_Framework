package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "item_refeicao")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRefeicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String alimento;
    private double quantidade;
    private String unidadeMedida;

    @Column(columnDefinition = "JSON")
    private String resumoNutricional;

    @ManyToOne
    @JoinColumn(name = "entrada_id")
    private EntradaPlanejamento entrada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refeicao_id", nullable = false) // Define a coluna da chave estrangeira
    @JsonIgnore // Evita loops infinitos de JSON se usar @Data
    private Refeicao refeicao;
}
