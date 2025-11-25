package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profissionais")
@Inheritance(strategy = InheritanceType.JOINED) 
@Getter @Setter @NoArgsConstructor 
public abstract class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Column(name = "registro_profissional", unique = true)
    private String registroProfissional;

    @ManyToMany
    @JoinTable(
            name = "profissional_pacientes",
            joinColumns = @JoinColumn(name = "profissional_id"),
            inverseJoinColumns = @JoinColumn(name = "paciente_id")
    )
    private List<Paciente> pacientes = new ArrayList<>();

    public abstract String getTipoUsuario();
}
