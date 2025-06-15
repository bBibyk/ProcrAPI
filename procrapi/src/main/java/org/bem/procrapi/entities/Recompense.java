package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;

import java.util.ArrayList;
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

    @JsonIgnore
    @OneToMany(mappedBy = "recompense")
    private List<AttributionRecompense> attributions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy= "recompense")
    private List<PiegeDeProductivite> pieges = new ArrayList<>();
}
