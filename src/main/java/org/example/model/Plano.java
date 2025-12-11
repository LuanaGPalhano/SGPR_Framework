package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Importante para a herança funcionar no banco
@Data
public abstract class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objetivo;
    private LocalDate dataInicio;

    // Todo plano pertence a um paciente (Ponto Fixo)
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}