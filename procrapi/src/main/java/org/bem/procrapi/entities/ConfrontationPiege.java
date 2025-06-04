package org.bem.procrapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;

import java.util.Date;

@Getter
@Setter
@Entity
public class ConfrontationPiege {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateConfrontation;

    @Enumerated(EnumType.ORDINAL)
    private ResultatConfrontationPiege resultat;

    private Integer points;

    private String commentaire;

    @ManyToOne
    private PiegeDeProductivite piege;

    @ManyToOne
    private Utilisateur utilisateur;
}
