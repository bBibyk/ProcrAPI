package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.StatutTache;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TacheAEviter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id; // Identifiant unique de la tâche

    private String titre; // Intitulé de la tâche à éviter (doit être explicite)

    private String description = ""; // Détail ou contexte supplémentaire sur la tâche

    private Integer degreUrgence = 1; // Urgence perçue de la tâche (plus c’est élevé, plus elle appelle à être faite rapidement)

    @Temporal(TemporalType.DATE)
    private LocalDate dateLimite; // Date limite attendue de réalisation (sert à la tentation)

    private String consequences = ""; // Ce qu'on risque (ou croit risquer) à ne pas faire cette tâche

    // Attribut ajouté afin de calculer la règle métier de déchéance
    @Temporal(TemporalType.DATE)
    private LocalDate dateCompletion; // Date à laquelle l’utilisateur a "cédé" et effectué la tâche (si applicable)

    @Enumerated(EnumType.ORDINAL)
    private StatutTache statut = StatutTache.EN_ATTENTE; // Statut (en attente, évitée, échouée...)

    @Temporal(TemporalType.DATE)
    private LocalDate dateCreation = LocalDate.now(); // Date à laquelle la tâche a été ajoutée par l’utilisateur

    @ManyToOne
    private Utilisateur utilisateur; // Propriétaire de la tâche à éviter
}
