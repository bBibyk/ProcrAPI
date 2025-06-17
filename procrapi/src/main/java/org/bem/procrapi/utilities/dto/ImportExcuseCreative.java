package org.bem.procrapi.utilities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;

@Data
@NoArgsConstructor
public class ImportExcuseCreative {
    private String texte;
    private String situation;
    private CategorieExcuse categorie;
}
