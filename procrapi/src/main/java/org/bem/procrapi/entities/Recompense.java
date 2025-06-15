package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une récompense que peut recevoir un utilisateur.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recompense {

    /**
     * Identifiant unique de la récompense.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Titre de la récompense.
     */
    private String titre;

    /**
     * Description de la récompense.
     */
    private String description;

    /**
     * Conditions requises pour obtenir cette récompense.
     */
    private String conditionsObtention;

    /**
     * Niveau de prestige de la récompense (BRONZE, ARGENT, OR...).
     */
    @Enumerated(EnumType.STRING)
    private NiveauDePrestige niveau;

    /**
     * Type de récompense (badge, médaille, trophée...).
     */
    @Enumerated(EnumType.STRING)
    private TypeRecompense type;

    /**
     * Liste des attributions de cette récompense à des utilisateurs.
     * Ignorée en JSON pour éviter les boucles de sérialisation.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "recompense")
    private List<AttributionRecompense> attributions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy= "recompense")
    private List<PiegeDeProductivite> pieges = new ArrayList<>();
}
