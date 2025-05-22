package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.bem.procrapi.utilities.TypeRecompense;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recompense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titre;
    private String description;
    private String conditionsObtention;
    private String niveauPrestige;

    @Enumerated(EnumType.STRING)
    private TypeRecompense type;
}
