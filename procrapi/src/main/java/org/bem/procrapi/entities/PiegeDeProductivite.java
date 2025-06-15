package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.StatutPiege;
import org.bem.procrapi.utilities.enumerations.TypePiege;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class PiegeDeProductivite {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titre;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private TypePiege type;

    private Integer difficulte;

    @ManyToOne
    private Recompense recompense;

    private String consequence = "";

    @Temporal(TemporalType.DATE)
    private LocalDate dateCreation = LocalDate.now();

    @Enumerated(EnumType.ORDINAL)
    private StatutPiege statut = StatutPiege.ACTIF;

    @ManyToOne
    private Utilisateur createur;

    @JsonIgnore
    @OneToMany(mappedBy = "piege")
    private List<ConfrontationPiege> confrontations = new ArrayList<>();

}

