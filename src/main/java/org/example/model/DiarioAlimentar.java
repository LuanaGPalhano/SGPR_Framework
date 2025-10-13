package org.example.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "diario_alimentar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DiarioAlimentar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imgURL;

    @OneToMany(mappedBy = "diario" ,cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Refeicoes> refeicoes = new java.util.ArrayList<>();
}