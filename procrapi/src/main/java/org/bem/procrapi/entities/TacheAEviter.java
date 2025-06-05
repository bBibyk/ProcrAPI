package org.bem.procrapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.StatutTache;

import java.util.Date;

@Entity
@Getter
@Setter
public class TacheAEviter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titre;

    private String description = "";

    private Integer degreUrgence = 1;

    @Temporal(TemporalType.DATE)
    private Date dateLimite;

    private String consequences = "";

    @Enumerated(EnumType.ORDINAL)
    private StatutTache statut = StatutTache.EN_ATTENTE;

    @Temporal(TemporalType.DATE)
    private Date dateCreation = new Date();

    @ManyToOne
    private Utilisateur utilisateur;
}
