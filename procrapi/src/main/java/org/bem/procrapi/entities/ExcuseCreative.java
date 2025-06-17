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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String texte = "";

    private String situation = "";

    private Integer votesRecus = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "excusePreferee")
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    // Pour éviter de circulariser le JSON au cas ou le createur a pour Excuse préféree sa propre excuse
    // On fait la sérialisation côté excuse
    @JsonManagedReference
    @ManyToOne
    private Utilisateur createur;

    @Temporal(TemporalType.DATE)
    private LocalDate dateSoumission = LocalDate.now();

    @Enumerated(EnumType.ORDINAL)
    private CategorieExcuse categorie = CategorieExcuse.VIE_SOCIALE;

    @Enumerated(EnumType.ORDINAL)
    private StatutExcuse statut = StatutExcuse.EN_ATTENTE;
}
