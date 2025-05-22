package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.DifficulteDefi;
import org.bem.procrapi.utilities.StatutDefi;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class DefiDeProcrastination {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titre;

    private String description;

    private Integer duree;

    @Enumerated(EnumType.ORDINAL)
    private DifficulteDefi difficulte;

    private Integer pointsAGagner;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @Enumerated(EnumType.ORDINAL)
    private StatutDefi statut;

    @ManyToOne
    private Utilisateur createur;

    @OneToMany(mappedBy = "defi")
    private List<ParticipationDefi> participations;
}
