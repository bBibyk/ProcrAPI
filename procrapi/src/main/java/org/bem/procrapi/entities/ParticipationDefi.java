package org.bem.procrapi.entities;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;

import java.util.Date;

@Getter
@Setter
@Entity
public class ParticipationDefi {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateInscription;

    @Enumerated(EnumType.ORDINAL)
    private StatutParticipation statut;

    private Integer points;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private DefiDeProcrastination defi;
}
