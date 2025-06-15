package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Integer duree = 1;

    @Enumerated(EnumType.ORDINAL)
    private DifficulteDefi difficulte = DifficulteDefi.FACILE;

    private Integer pointsAGagner = 0;

    @Temporal(TemporalType.DATE)
    private LocalDate dateDebut;

    //date de décompte des résultats
    //attribut calculé à partir de la durée
    @Temporal(TemporalType.DATE)
    private LocalDate dateFin;

    @Enumerated(EnumType.ORDINAL)
    private StatutDefi statut = StatutDefi.INSCRIT;

    @ManyToOne
    private Utilisateur createur;

    @JsonIgnore
    @OneToMany(mappedBy = "defi")
    private List<ParticipationDefi> participations = new ArrayList<>();
}
