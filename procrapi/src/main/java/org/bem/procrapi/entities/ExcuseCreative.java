package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
 * Entité représentant une excuse créative.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseCreative {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id; // Identifiant unique de l'excuse

    private String texte = ""; // Contenu textuel de l'excuse

    private String situation = ""; // Situation ou contexte justifiant l'excuse

    private Integer votesRecus = 0; // Nb de votes reçus par la communauté (indicateur de popularité)

    @JsonIgnore
    @OneToMany(mappedBy = "excusePreferee")
    private List<Utilisateur> utilisateurs = new ArrayList<>(); // Utilisateurs ayant choisi cette excuse comme préférée

    // Pour éviter de circulariser le JSON au cas où le créateur a pour excuse préférée sa propre excuse
    // On fait la sérialisation côté excuse
    @JsonManagedReference
    @ManyToOne
    private Utilisateur createur; // Auteur de l'excuse

    @Temporal(TemporalType.DATE)
    private LocalDate dateSoumission = LocalDate.now(); // Date de soumission de l'excuse (par défaut aujourd'hui)

    @Enumerated(EnumType.ORDINAL)
    private CategorieExcuse categorie = CategorieExcuse.VIE_SOCIALE; // Catégorie de l'excuse (vie sociale, fatigue, etc.)

    @Enumerated(EnumType.ORDINAL)
    private StatutExcuse statut = StatutExcuse.EN_ATTENTE; // Statut modération (en attente, validée, rejetée)

}
