package org.example.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "diario_base")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

//Diario é fixo para qualquer aplicação

public class DiarioBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    @Builder.Default
    private java.time.LocalDateTime registroHorario = java.time.LocalDateTime.now();

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imgURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;


    @OneToMany(mappedBy = "diario" ,cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EntradaDiario> entradasDiario = new java.util.ArrayList<>();
}