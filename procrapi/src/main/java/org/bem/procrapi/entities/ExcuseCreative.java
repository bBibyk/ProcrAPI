package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une excuse créative proposée par un utilisateur.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseCreative {

    /**
     * Identifiant unique de l'excuse.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Contenu textuel de l'excuse.
     */
    private String texte = "";

    /**
     * Situation ou contexte dans lequel l’excuse est utilisée.
     */
    private String situation = "";

    /**
     * Nombre de votes reçus pour cette excuse.
     */
    private Integer votesRecus = 0;

    /**
     * Utilisateurs ayant défini cette excuse comme leur préférée.
     * Ignoré en JSON pour éviter les cycles.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "excusePreferee")
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    /**
     * Utilisateur ayant créé cette excuse.
     */
    @ManyToOne
    private Utilisateur createur;

    /**
     * Date à laquelle l’excuse a été soumise.
     */
    @Temporal(TemporalType.DATE)
    private LocalDate dateSoumission;

    /**
     * Catégorie à laquelle appartient cette excuse.
     */
    @Enumerated(EnumType.ORDINAL)
    private CategorieExcuse categorie;

    /**
     * Statut de modération de l’excuse (en attente, approuvée ou rejetée).
     */
    @Enumerated(EnumType.ORDINAL)
    private StatutExcuse statut;
}
