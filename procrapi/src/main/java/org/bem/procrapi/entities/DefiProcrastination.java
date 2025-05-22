package org.bem.procrapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.DifficulteDefi;
import org.bem.procrapi.utilities.StatutDefi;

import java.util.Date;

@Entity
@Getter
@Setter
public class DefiProcrastination {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String titre;

    private String description;

    private int duree;

    private DifficulteDefi difficulte;

    private int pointsAGagner;

    private Date dateDebut;

    private Date dateFin;

    private StatutDefi statut;
}
