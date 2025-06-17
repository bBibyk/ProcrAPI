package org.bem.procrapi.utilities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.StatutTache;

@Data
@NoArgsConstructor
public class ImportSetStatutTache {
    private String titreTache;
    private StatutTache statut;
}
