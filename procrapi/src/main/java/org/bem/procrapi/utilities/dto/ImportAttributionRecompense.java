package org.bem.procrapi.utilities.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;

import java.time.LocalDate;

@Data
public class ImportAttributionRecompense {
    private LocalDate dateExpiration;

    private String contexteAttribution;

    private Utilisateur utilisateur;

    private Recompense recompense;
}
