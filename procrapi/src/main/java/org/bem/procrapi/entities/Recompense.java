package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recompense {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titre;

    private String description;

    private String conditionsObtention;

    @Enumerated(EnumType.STRING)
    private NiveauDePrestige niveau;

    @Enumerated(EnumType.STRING)
    private TypeRecompense type;

    @OneToMany(mappedBy = "recompense")
    private List<AttributionRecompense> attributions;
}
