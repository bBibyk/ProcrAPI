package org.bem.procrapi.utilities.dto;

import lombok.Data;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;

@Data
public class ImportSetStatutExcuse {
    private String texteExcuse;
    private StatutExcuse statut;
}
