package org.bem.procrapi.services;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutDefi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ServiceDefiDeProcrastination {
    private RepositoryDefiDeProcrastination repositoryDefiDeProcrastination;

    @Autowired
    public ServiceDefiDeProcrastination(RepositoryDefiDeProcrastination repositoryDefiDeProcrastination) {
        this.repositoryDefiDeProcrastination = repositoryDefiDeProcrastination;
    }


    public DefiDeProcrastination create(DefiDeProcrastination defi){
        if(defi.getDateFin()==null){
            throw new IllegalArgumentException("La date de fin doit être spécifiée");
        } else if (defi.getDateDebut()==null) {
            throw new IllegalArgumentException("La date de début doit être spécifiée");
        } else if (!defi.getDateFin().after(defi.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        } else if (!defi.getDateDebut().before(new Date())) {
            throw new IllegalArgumentException("La date de début doit être ultérieure à la date d'ajourd'hui");
        } else if (defi.getTitre()==null) {
            throw new IllegalArgumentException("Le titre doit être spécifié");
        } else if (defi.getDuree()==null) {
            throw new IllegalArgumentException("La durée doit être spécifiée");
        } else if (defi.getPointsAGagner()==null || defi.getPointsAGagner()<=0) {
            throw new IllegalArgumentException("Les points à gagner doivent être >= 0");
        } else if (UtilisateurHolder.getCurrentUser() == null){
            throw new IllegalArgumentException("Vous n'êtes pas authentifié");
        } else if (UtilisateurHolder.getCurrentUser().getRole() != RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new IllegalArgumentException("Vous n'avez pas ce droit");
        }
        DefiDeProcrastination savedDefi = new DefiDeProcrastination();
        savedDefi.setDateDebut(defi.getDateDebut());
        savedDefi.setDateFin(defi.getDateFin());
        savedDefi.setTitre(defi.getTitre());
        savedDefi.setDuree(defi.getDuree());
        savedDefi.setCreateur(UtilisateurHolder.getCurrentUser());
        savedDefi.setPointsAGagner(defi.getPointsAGagner());
        if(defi.getDescription()!=null){
            savedDefi.setDescription(defi.getDescription());
        }
        if(defi.getDifficulte()!=null){
            savedDefi.setDifficulte(defi.getDifficulte());
        }
        return repositoryDefiDeProcrastination.save(defi);
    }

    public DefiDeProcrastination setStatut(DefiDeProcrastination defi, StatutDefi statut){
        defi.setStatut(statut);
        return repositoryDefiDeProcrastination.save(defi);
    }
}