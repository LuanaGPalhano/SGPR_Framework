package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "educadores_fisicos")
@Getter @Setter @NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id") // Garante a ligação com a tabela pai pelo ID
public class EducadorFisico extends Profissional {

    @Column(name = "cref", unique = true)
    private String cref;

    @Override
    public String getTipoUsuario() {
        return "EDUCADOR_FISICO";
    }

    // Relacionamentos específicos (Dieta, etc) continuam aqui...
    @OneToMany(mappedBy = "educadorFisico", cascade = CascadeType.ALL)
    private java.util.List<Treino> treinos;
}