package org.bem.procrapi.utilities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;

@NoArgsConstructor
@Data
public class ImportSetStatutExcuse {
    private String texteExcuse;
    private StatutExcuse statut;
}
