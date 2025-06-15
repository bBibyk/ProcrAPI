package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ConfrontationPiege {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate dateConfrontation=LocalDate.now();

    @Enumerated(EnumType.ORDINAL)
    private ResultatConfrontationPiege resultat;

    private Integer points;

    private String commentaire;

    @ManyToOne
    private PiegeDeProductivite piege;

    @ManyToOne
    private Utilisateur utilisateur;
}
