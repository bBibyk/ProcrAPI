package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ParticipationDefi {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id; // Identifiant unique de la participation

    @Temporal(TemporalType.DATE)
    private LocalDate dateInscription = LocalDate.now(); // Date à laquelle l'utilisateur s'est inscrit au défi (par défaut = aujourd'hui)

    @Enumerated(EnumType.ORDINAL)
    private StatutParticipation statut; // Statut de l'inscription (Ex : EN_COURS, TERMINE, ABANDON)

    private Integer points = 0; // Points obtenus par l'utilisateur pour ce défi (départ à 0)

    @ManyToOne
    private Utilisateur utilisateur; // L'utilisateur qui participe au défi

    @ManyToOne
    private DefiDeProcrastination defi; // Le défi concerné

}
