package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.DifficulteDefi;
import org.bem.procrapi.utilities.enumerations.StatutDefi;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class DefiDeProcrastination {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titre = "";

    private String description = "";

    // minutes -> décision arbitraire pour mieux tester
    // temps de durée pour participer
    private Integer duree;

    @Enumerated(EnumType.ORDINAL)
    private DifficulteDefi difficulte = DifficulteDefi.FACILE;

    private Integer pointsAGagner;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    //date de décompte des résultats
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @Enumerated(EnumType.ORDINAL)
    private StatutDefi statut = StatutDefi.INSCRIT;

    @ManyToOne
    private Utilisateur createur;

    @OneToMany(mappedBy = "defi")
    private List<ParticipationDefi> participations;
}
