package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import org.example.enums.ProfissionalTipo;

@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataAvaliacao;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    private ProfissionalTipo tipoProfissional; 

    // PONTO VARIÁVEL: Armazena o objeto específico serializado em JSON
    @Lob
    @Column(columnDefinition = "TEXT")
    private String dadosEspecificos; 
}
