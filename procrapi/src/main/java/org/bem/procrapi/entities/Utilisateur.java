package org.bem.procrapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.NiveauProcrastination;
import org.bem.procrapi.utilities.RoleUtilisateur;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String pseudo;

    private NiveauProcrastination niveau;

    @Temporal(TemporalType.DATE)
    private Date dateInscription;

    private Integer pointsAccumules;

    @ManyToOne
    private ExcuseCreative excusePreferee;

    @OneToMany
    private List<ExcuseCreative> excuses;

    private String email;

    private RoleUtilisateur role;

    @OneToMany(mappedBy = "utilisateur")
    private List<TacheAEviter> taches;

    @OneToMany(mappedBy = "createur")
    private List<DefiDeProcrastination> defis;

    @OneToMany(mappedBy = "createur")
    private List<PiegeDeProductivite> pieges;

    @OneToMany(mappedBy = "utilisateur")
    private List<ConfrontationPiege> confrontations;

    @OneToMany(mappedBy = "utilisateur")
    private List<AttributionRecompense> recompenses;

    @OneToMany(mappedBy = "utilisateur")
    private List<ParticipationDefi> participations;
}
