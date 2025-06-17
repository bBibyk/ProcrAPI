package org.bem.procrapi.utilities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;

@Data
@NoArgsConstructor
public class ImportUtilisateur {
    private RoleUtilisateur role;
    private String pseudo;
    private String email;
    private ImportExcuseCreative excusePreferee;
}
