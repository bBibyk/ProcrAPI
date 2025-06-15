package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.DifficulteDefi;
import org.bem.procrapi.utilities.enumerations.StatutDefi;

import java.time.LocalDate;
import java.util.ArrayList;
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

    // attribut un peu inutile car dateFin existe et est mieux
    private Integer duree;

    @Enumerated(EnumType.ORDINAL)
    private DifficulteDefi difficulte = DifficulteDefi.FACILE;

    private Integer pointsAGagner;

    @Temporal(TemporalType.DATE)
    private LocalDate dateDebut;

    //date de décompte des résultats
    @Temporal(TemporalType.DATE)
    private LocalDate dateFin;

    @Enumerated(EnumType.ORDINAL)
    private StatutDefi statut = StatutDefi.INSCRIT;

    @ManyToOne
    private Utilisateur createur;

    @OneToMany(mappedBy = "defi")
    private List<ParticipationDefi> participations = new ArrayList<>();
}
