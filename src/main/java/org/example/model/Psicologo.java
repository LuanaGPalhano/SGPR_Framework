package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "psicologos")
@Getter @Setter @NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id") // Garante a ligação com a tabela pai pelo ID
public class Psicologo extends Profissional {

    @Column(name = "crp", unique = true)
    private String crp;

    @Override
    public String getTipoUsuario() {
        return "PSICOLOGO";
    }

    // Relacionamentos específicos (Dieta, etc) continuam aqui...
    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL)
    private java.util.List<PlanoTerapeutico> planoTerapeuticos;
}