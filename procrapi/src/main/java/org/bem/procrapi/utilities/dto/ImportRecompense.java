package org.bem.procrapi.utilities.dto;

import lombok.Data;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;

@Data
public class ImportRecompense {
    private String titre;
    private String description;
    private String conditionsObtention;
    private NiveauDePrestige niveau;
    private TypeRecompense type;
}
