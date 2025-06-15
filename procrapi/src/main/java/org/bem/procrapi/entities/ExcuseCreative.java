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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String texte;

    private String situation;

    private int votesRecus;

    @JsonIgnore
    @OneToMany(mappedBy = "excusePreferee")
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    @ManyToOne
    private Utilisateur createur;

    @Temporal(TemporalType.DATE)
    private LocalDate dateSoumission;

    @Enumerated(EnumType.ORDINAL)
    private CategorieExcuse categorie;

    @Enumerated(EnumType.ORDINAL)
    private StatutExcuse statut;
}
