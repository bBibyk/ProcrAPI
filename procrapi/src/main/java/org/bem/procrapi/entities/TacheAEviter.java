package org.bem.procrapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.StatutTache;

import java.util.Date;

@Entity
@Getter
@Setter
public class TacheAEviter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titre;

    @NotNull
    private String description;

    @Min(1)
    @Max(5)
    private Integer degreUrgence;

    @Temporal(TemporalType.DATE)
    private Date dateLimite;

    private String consequences;

    @Enumerated(EnumType.ORDINAL)
    private StatutTache statut;

    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @ManyToOne
    private Utilisateur utilisateur;
}
