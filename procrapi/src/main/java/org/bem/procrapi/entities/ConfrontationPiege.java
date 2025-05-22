package org.bem.procrapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.sql.Date;

@Getter
@Setter
@Entity
public class ConfrontationPiege {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long piegeID; // ID du piège productivité confronté

    private Long utilisateurID; // ID de l'utilisateur confronté

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateConfrontation;

    private String resultat; // par ex. "succès" ou "échec"

    private int pointsGagnesPerdus;

    private String commentaire;
}
