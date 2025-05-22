package org.bem.procrapi.entities;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.bem.procrapi.utilities.StatutParticipation;

import java.sql.Date;

@Getter
@Setter
@Entity
public class ParticipationDefi {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long defiID; // ID du défi

    private Long utilisateurID; // ID de l'utilisateur participant

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInscription;

    @Enumerated(EnumType.STRING)
    private StatutParticipation statut;

    private int pointsGagnes;
}
