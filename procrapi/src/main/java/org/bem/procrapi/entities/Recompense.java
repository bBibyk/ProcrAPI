package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.bem.procrapi.utilities.NiveauDePrestige;
import org.bem.procrapi.utilities.TypeRecompense;

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

    @Enumerated(EnumType.ORDINAL)
    private NiveauDePrestige niveau;

    @Enumerated(EnumType.ORDINAL)
    private TypeRecompense type;

    @OneToMany(mappedBy = "recompense")
    private List<Recompense> attributions;
}
