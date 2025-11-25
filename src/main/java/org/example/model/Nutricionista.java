package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nutricionistas")
@Getter @Setter @NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id") // Garante a ligação com a tabela pai pelo ID
public class Nutricionista extends Profissional {

    @Column(name = "crn_uf", unique = true)
    private String crn;

    @Override
    public String getTipoUsuario() {
        return "NUTRICIONISTA";
    }

    // Relacionamentos específicos (Dieta, etc) continuam aqui...
    @OneToMany(mappedBy = "nutricionista", cascade = CascadeType.ALL)
    private java.util.List<Dieta> dietas;
}