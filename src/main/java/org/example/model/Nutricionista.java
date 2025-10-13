package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nutricionistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nutricionista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String crnUf;
    @Column(unique = true)
    private String email;
    private String senha;

    public Nutricionista(String nome, String crnUf, String email, String senha) {
        this.nome = nome;
        this.crnUf = crnUf;
        this.email = email;
        this.senha = senha;
    }

}