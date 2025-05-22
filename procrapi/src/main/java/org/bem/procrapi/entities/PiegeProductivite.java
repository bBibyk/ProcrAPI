package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.StatutPiege;
import org.bem.procrapi.utilities.TypePiege;


import java.sql.Date;

@Getter
@Setter
@Entity
public class PiegeProductivite {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titre;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TypePiege type;

    private Long createurID;

    private String difficulte;

    private String recompense;

    private String consequence;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Enumerated(EnumType.STRING)
    private StatutPiege statut;

}

