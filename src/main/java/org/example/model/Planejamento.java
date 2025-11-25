package org.example.model;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "planejamento_semanal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planejamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descricao;

    @OneToMany(mappedBy = "planejamento" ,cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EntradaPlanejamento> entradas = new ArrayList<>() ;
}
