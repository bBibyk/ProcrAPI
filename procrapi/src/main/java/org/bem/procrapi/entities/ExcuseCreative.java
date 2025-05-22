package org.bem.procrapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bem.procrapi.utilities.CategorieExcuse;
import org.bem.procrapi.utilities.StatutExcuse;

import java.util.Date;
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

    private String votesRecus;

    @OneToMany(mappedBy = "excusePreferee")
    private List<Utilisateur> utilisateurs;

    @ManyToOne
    private Utilisateur createur;

    @Temporal(TemporalType.DATE)
    private Date dateSoumission;

    @Enumerated(EnumType.ORDINAL)
    private CategorieExcuse categorie;

    @Enumerated(EnumType.ORDINAL)
    private StatutExcuse statut;
}
