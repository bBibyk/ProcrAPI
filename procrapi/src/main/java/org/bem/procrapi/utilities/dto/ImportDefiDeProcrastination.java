package org.bem.procrapi.utilities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.DifficulteDefi;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ImportDefiDeProcrastination {
    private LocalDate dateDebut;
    private String titre;
    private Integer duree;
    private Integer pointsAGagner;
    private String description;
    private DifficulteDefi difficulte;
}
