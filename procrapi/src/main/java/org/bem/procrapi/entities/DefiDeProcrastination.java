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

/**
 * Entité représentant un défi de procrastination.
 */
@Entity
@Getter
@Setter
public class DefiDeProcrastination {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id; // Identifiant unique du défi

    private String titre = ""; // Titre du défi (non nul par défaut)

    private String description = ""; // Description textuelle du défi

    private Integer duree = 1; // Durée du défi en jours (1 par défaut)

    @Enumerated(EnumType.ORDINAL)
    private DifficulteDefi difficulte = DifficulteDefi.FACILE; // Difficulté du défi (par défaut = FACILE)

    private Integer pointsAGagner = 0; // Points accordés en cas de réussite

    @Temporal(TemporalType.DATE)
    private LocalDate dateDebut; // Date de début du défi (doit être fixée à la création)

    @Temporal(TemporalType.DATE)
    private LocalDate dateFin; // Date de fin du défi (logiquement = dateDebut + durée)

    @Enumerated(EnumType.ORDINAL)
    private StatutDefi statut = StatutDefi.INSCRIT; // Statut global du défi (modifiable selon phase : actif, terminé...)

    @ManyToOne
    private Utilisateur createur; // L'utilisateur ayant créé ce défi

    @JsonIgnore
    @OneToMany(mappedBy = "defi")
    private List<ParticipationDefi> participations = new ArrayList<>(); // Liste des participations associées

}
