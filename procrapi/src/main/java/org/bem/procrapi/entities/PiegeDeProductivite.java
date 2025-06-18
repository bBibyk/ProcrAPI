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
    private Long id; // Identifiant unique du piège

    private String titre; // Titre explicite du piège

    private String description; // Description détaillée du piège

    @Enumerated(EnumType.ORDINAL)
    private TypePiege type; // Type de piège (ex : numérique, environnemental, psychologique...)

    private Integer difficulte; // Niveau de difficulté à éviter ce piège (à interpréter selon le contexte)

    @ManyToOne
    private Recompense recompense; // Récompense liée à la réussite d'une confrontation à ce piège

    private String consequence = ""; // Conséquence potentielle si l'utilisateur "tombe" dans le piège

    @Temporal(TemporalType.DATE)
    private LocalDate dateCreation = LocalDate.now(); // Date de création du piège (auto initialisée)

    @Enumerated(EnumType.ORDINAL)
    private StatutPiege statut = StatutPiege.ACTIF; // Statut du piège (actif/inactif)

    @ManyToOne
    private Utilisateur createur; // Utilisateur ayant soumis ce piège

    @JsonIgnore
    @OneToMany(mappedBy = "piege")
    private List<ConfrontationPiege> confrontations = new ArrayList<>(); // Historique des confrontations à ce piège

}
