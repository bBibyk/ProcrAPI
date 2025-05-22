package org.bem.procrapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.NiveauProcrastination;
import org.bem.procrapi.utilities.RoleUtilisateur;

import java.util.Date;

@Getter
@Setter
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String pseudo;

    private NiveauProcrastination niveau;

    private Date dateInscription;

    private Integer pointsAccumules;

    @ManyToOne
    @JoinColumn(name = "excuse_creative_id")
    private ExcuseCreative excusePreferee;

    private String email;

    @NotNull
    private RoleUtilisateur role;
}
