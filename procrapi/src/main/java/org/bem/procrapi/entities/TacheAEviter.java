package org.bem.procrapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private long id;

    private String description;

    private int degreUrgence;

    private Date dateLimite;

    private String consequences;

    private StatutTache statut;

    private Date dateCreation;
}
