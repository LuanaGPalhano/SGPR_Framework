package org.example.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType; // Importe Getter
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; // Importe Setter
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dieta")
@Getter // Use @Getter
@Setter // Use @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dieta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String objetivo;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "nutricionista_id")
    private Nutricionista nutricionista;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @OneToMany(mappedBy = "dieta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Refeicao> refeicoes = new HashSet<>();

}