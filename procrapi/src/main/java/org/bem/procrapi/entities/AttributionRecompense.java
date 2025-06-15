package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;

import java.time.LocalDate;

/**
 * Entité représentant l’attribution d’une récompense à un utilisateur.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributionRecompense {

    /**
     * Identifiant unique de l’attribution.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Date à laquelle l’utilisateur a obtenu la récompense.
     */
    @Temporal(TemporalType.DATE)
    private LocalDate dateObtention;

    /**
     * Date d’expiration de la récompense (optionnelle).
     */
    @Temporal(TemporalType.DATE)
    private LocalDate dateExpiration;

    /**
     * Contexte dans lequel la récompense a été attribuée.
     */
    private String contexteAttribution;

    /**
     * Statut actuel de la récompense (actif, expiré...).
     */
    @Enumerated(EnumType.ORDINAL)
    private StatutRecompense statut;

    /**
     * Utilisateur ayant reçu la récompense.
     */
    @ManyToOne
    private Utilisateur utilisateur;

    /**
     * Récompense concernée par l’attribution.
     */
    @ManyToOne
    private Recompense recompense;
}
