package org.bem.procrapi.utilities.dto;

import lombok.Data;
import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;

@Data
public class ImportUtilisateur {
    private RoleUtilisateur role;
    private String pseudo;
    private String email;
    private ExcuseCreative excusePreferee;
}
