package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;

import java.time.LocalDate;

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
    private LocalDate dateObtention;

    @Temporal(TemporalType.DATE)
    private LocalDate dateExpiration;

    private String contexteAttribution;

    @Enumerated(EnumType.ORDINAL)
    private StatutRecompense statut;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Recompense recompense;
}
