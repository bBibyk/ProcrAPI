package org.bem.procrapi.utilities.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ImportTacheAEviter {
    private LocalDate dateLimite;
    private String titre;
    private Integer degreUrgence;
    private String consequences;
    private String description;
}
