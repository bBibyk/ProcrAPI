package org.bem.procrapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String email;

    private RoleUtilisateur role;

    private String pseudo;

    private NiveauProcrastination niveau = NiveauProcrastination.DEBUTANT;

    @Temporal(TemporalType.DATE)
    private Date dateInscription = new Date();

    private Integer pointsAccumules = 0;

    @ManyToOne
    private ExcuseCreative excusePreferee;

    @OneToMany
    private List<ExcuseCreative> excuses = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur")
    private List<TacheAEviter> taches = new ArrayList<>();

    @OneToMany(mappedBy = "createur")
    private List<DefiDeProcrastination> defis = new ArrayList<>();

    @OneToMany(mappedBy = "createur")
    private List<PiegeDeProductivite> pieges = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur")
    private List<ConfrontationPiege> confrontations = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur")
    private List<AttributionRecompense> recompenses = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur")
    private List<ParticipationDefi> participations = new ArrayList<>();
}
