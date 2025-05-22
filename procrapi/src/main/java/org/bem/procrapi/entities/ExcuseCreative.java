package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.bem.procrapi.utilities.CategorieExcuse;
import org.bem.procrapi.utilities.StatutExcuse;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuseCreative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String texte;
    private String situation;
    private String sousRecu;

    private int auteurID; // ou @ManyToOne si tu veux un lien vers Utilisateur

    @Temporal(TemporalType.DATE)
    private Date dateSoumission;

    @Enumerated(EnumType.STRING)
    private CategorieExcuse categorie;

    @Enumerated(EnumType.STRING)
    private StatutExcuse statut;
}
