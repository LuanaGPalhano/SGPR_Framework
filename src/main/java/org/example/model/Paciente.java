package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;

    private String senha;

    // =========================================================================
    // 1. RELACIONAMENTO COM PROFISSIONAIS (PONTO FIXO)
    // =========================================================================

    @ManyToMany(mappedBy = "pacientes")
    private List<Profissional> profissionais = new ArrayList<>();

    // =========================================================================
    // 2. PRONTUÁRIO E PLANOS
    // =========================================================================

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes; // Já está genérico (Nutricional ou Física)

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Historico historico; // Ou FichaIndividual

    // AJUSTE RECOMENDADO: Mudar de 'Dieta' para 'Plano'
    // Assim, essa lista aceita tanto Dieta quanto Treino.
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dieta> dietas;
    // Se você mantiver List<Dieta>, o paciente não conseguirá ver os Treinos aqui.


    public Paciente(String nome, String cpf, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }
}