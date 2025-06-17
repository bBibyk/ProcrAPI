package org.bem.procrapi.utilities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;

@Data
@NoArgsConstructor
public class ImportConfrontationPiege {
    private ImportPiegeDeProductivite piege;
    private ResultatConfrontationPiege resultat;
}
