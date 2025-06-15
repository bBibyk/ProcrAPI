package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.StatutTache;

import java.time.LocalDate;

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
    private LocalDate dateLimite;

    private String consequences = "";

    @Temporal(TemporalType.DATE)
    private LocalDate dateCompletion;

    @Enumerated(EnumType.ORDINAL)
    private StatutTache statut = StatutTache.EN_ATTENTE;

    @Temporal(TemporalType.DATE)
    private LocalDate dateCreation = LocalDate.now();

    @ManyToOne
    private Utilisateur utilisateur;
}
