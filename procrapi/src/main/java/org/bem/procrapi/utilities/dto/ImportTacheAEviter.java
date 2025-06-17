package org.bem.procrapi.utilities.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ImportTacheAEviter {
    private LocalDate dateLimite;
    private String titre;
    private Integer degreUrgence;
    private String consequences;
    private String description;
}
