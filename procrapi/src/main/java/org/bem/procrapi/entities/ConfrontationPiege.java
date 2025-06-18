package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;

import java.time.LocalDate;

/**
 * Entité représentant une confrontation à un piège de productivité.
 */
@Getter
@Setter
@Entity
public class ConfrontationPiege {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id; // Identifiant unique de la confrontation

    @Temporal(TemporalType.DATE)
    private LocalDate dateConfrontation = LocalDate.now(); // Date de la confrontation (défaut = aujourd'hui)

    @Enumerated(EnumType.ORDINAL)
    private ResultatConfrontationPiege resultat; // Résultat de la confrontation (ex : SUCCES, ECHEC)

    private Integer points; // Points gagnés ou perdus selon le résultat

    private String commentaire; // Commentaire éventuel de l’utilisateur (subjectif)

    @ManyToOne
    private PiegeDeProductivite piege; // Le piège de productivité auquel l'utilisateur a été confronté

    @ManyToOne
    private Utilisateur utilisateur; // L'utilisateur concerné

}
