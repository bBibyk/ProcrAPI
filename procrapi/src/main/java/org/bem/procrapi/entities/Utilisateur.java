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
    private Long id; // Identifiant unique

    @NotNull
    private String email; // Email de connexion / d'identification (non null)

    private RoleUtilisateur role; // Rôle de l'utilisateur dans l'app (ex : USER, ADMIN)

    private String pseudo; // Nom affiché dans l'application

    private NiveauProcrastination niveau = NiveauProcrastination.DEBUTANT; // Niveau de progression ou de "sagesse" anti-procrastination

    private Integer pointsAccumules = 0; // Points obtenus via les défis ou confrontations

    @Temporal(TemporalType.DATE)
    private LocalDate dateInscription = LocalDate.now(); // Date d’inscription, auto-assignée

    // Référence à l'excuse préférée de l'utilisateur
    // JsonBackReference pour éviter les boucles d'infinite recursion côté JSON
    @JsonBackReference
    @ManyToOne
    private ExcuseCreative excusePreferee;

    // Excuses créées par cet utilisateur
    @JsonIgnore
    @OneToMany(mappedBy = "createur")
    private List<ExcuseCreative> excuses = new ArrayList<>();

    // Tâches de cet utilisateur
    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    private List<TacheAEviter> taches = new ArrayList<>();

    // Défis de procrastination créés par cet utilisateur
    @JsonIgnore
    @OneToMany(mappedBy = "createur")
    private List<DefiDeProcrastination> defis = new ArrayList<>();

    // Pièges soumis par cet utilisateur
    @JsonIgnore
    @OneToMany(mappedBy = "createur")
    private List<PiegeDeProductivite> pieges = new ArrayList<>();

    // Confrontations de l'utilisateur avec des pièges
    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    private List<ConfrontationPiege> confrontations = new ArrayList<>();

    // Récompenses obtenues par l'utilisateur
    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    private List<AttributionRecompense> recompenses = new ArrayList<>();

    // Participations aux défis
    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur")
    private List<ParticipationDefi> participations = new ArrayList<>();
}
