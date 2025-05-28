package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.StatutPiege;
import org.bem.procrapi.utilities.enumerations.TypePiege;


import java.util.Date;
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

    private String recompense;

    private String consequence;

    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Enumerated(EnumType.ORDINAL)
    private StatutPiege statut;

    @ManyToOne
    private Utilisateur createur;

    @OneToMany(mappedBy = "piege")
    private List<ConfrontationPiege> confrontations;
}

