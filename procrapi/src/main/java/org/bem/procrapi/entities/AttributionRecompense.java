package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.bem.procrapi.utilities.StatutRecompense;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributionRecompense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int recompenseID;
    private int utilisateurID;

    @Temporal(TemporalType.DATE)
    private Date dateObtention;

    @Temporal(TemporalType.DATE)
    private Date dateExpiration;

    private String contexteAttribution;

    @Enumerated(EnumType.STRING)
    private StatutRecompense statut;
}
