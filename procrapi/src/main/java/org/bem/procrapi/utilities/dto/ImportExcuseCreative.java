package org.bem.procrapi.utilities.dto;

import lombok.Data;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;

@Data
public class ImportExcuseCreative {
    private String texte;
    private String situation;
    private CategorieExcuse categorie;
}
