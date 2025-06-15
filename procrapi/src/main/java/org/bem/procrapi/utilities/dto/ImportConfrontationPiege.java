package org.bem.procrapi.utilities.dto;

import lombok.Data;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;

@Data
public class ImportConfrontationPiege {
    private ImportPiegeDeProductivite piege;
    private ResultatConfrontationPiege resultat;
    private Integer points;
}
