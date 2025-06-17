package org.bem.procrapi.utilities.dto;

import lombok.Data;
import org.bem.procrapi.utilities.enumerations.StatutTache;

@Data
public class ImportSetStatutTache {
    private String titreTache;
    private StatutTache statut;
}
