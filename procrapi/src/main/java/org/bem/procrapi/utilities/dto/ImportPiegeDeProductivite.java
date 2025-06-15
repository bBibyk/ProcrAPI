package org.bem.procrapi.utilities.dto;

import lombok.Data;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.utilities.enumerations.TypePiege;

@Data
public class ImportPiegeDeProductivite {
    private String titre;
    private TypePiege type;
    private Integer difficulte;
    private String description;
    private ImportRecompense recompense;
    private String consequence;
}
