package org.bem.procrapi.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ParticipationDefi {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate dateInscription;

    @Enumerated(EnumType.ORDINAL)
    private StatutParticipation statut;

    private Integer points;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private DefiDeProcrastination defi;
}
