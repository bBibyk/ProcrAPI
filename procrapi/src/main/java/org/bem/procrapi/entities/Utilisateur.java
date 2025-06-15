package org.bem.procrapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private Integer pointsAccumules = 0;

    @Temporal(TemporalType.DATE)
    private LocalDate dateInscription = LocalDate.now();

    @ManyToOne
    private ExcuseCreative excusePreferee;

    @JsonBackReference
    @OneToMany
    private List<ExcuseCreative> excuses = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "utilisateur")
    private List<TacheAEviter> taches = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "createur")
    private List<DefiDeProcrastination> defis = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "createur")
    private List<PiegeDeProductivite> pieges = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "utilisateur")
    private List<ConfrontationPiege> confrontations = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "utilisateur")
    private List<AttributionRecompense> recompenses = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "utilisateur")
    private List<ParticipationDefi> participations = new ArrayList<>();
    //TODO @JsonBackReference and init lists and sensible attributes
}
