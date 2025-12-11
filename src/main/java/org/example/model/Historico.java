package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.ProfissionalTipo;
import java.time.LocalDate;

@Entity
@Table(name = "historicos")
@Data
@NoArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- PONTOS FIXOS (Demográficos e Estilo de Vida) ---
    private String estadoCivil;
    private String ocupacao;
    private boolean bebe;
    private boolean fuma;
    private LocalDate dataPreenchimento;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    private ProfissionalTipo tipoProfissional;

    // --- PONTO VARIÁVEL (JSON) ---
    @Lob
    @Column(columnDefinition = "TEXT")
    private String dadosEspecificos; 
}
