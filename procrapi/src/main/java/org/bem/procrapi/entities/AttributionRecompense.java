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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateObtention;

    @Temporal(TemporalType.DATE)
    private Date dateExpiration;

    private String contexte;

    @Enumerated(EnumType.ORDINAL)
    private StatutRecompense statut;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Recompense recompense;
}
