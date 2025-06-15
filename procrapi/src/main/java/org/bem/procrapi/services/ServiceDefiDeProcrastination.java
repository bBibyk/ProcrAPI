package org.bem.procrapi.services;

import org.bem.procrapi.components.authentication.EmailHolder;
import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutDefi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServiceDefiDeProcrastination {
    private final RepositoryDefiDeProcrastination repositoryDefiDeProcrastination;
    private final ServiceUtilisateur utilisateurService;


    @Autowired
    public ServiceDefiDeProcrastination(RepositoryDefiDeProcrastination repositoryDefiDeProcrastination,
                                        ServiceUtilisateur utilisateurService) {
        this.utilisateurService = utilisateurService;
        this.repositoryDefiDeProcrastination = repositoryDefiDeProcrastination;
    }


    public DefiDeProcrastination create(DefiDeProcrastination defi){
        Utilisateur utilisateurCourant = utilisateurService.getUtilisateurCourant();
        if (utilisateurCourant.getRole() != RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new IllegalArgumentException("Seul le gestionnaire du temps perdu peut créer des défis.");
        } else if (defi.getDateDebut()==null) {
            throw new IllegalArgumentException("La date de début doit être spécifiée.");
        } else if (!defi.getDateFin().isAfter(defi.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        } else if (!defi.getDateDebut().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de début doit être postérieure à la date d'ajourd'hui.");
        } else if (defi.getTitre()==null) {
            throw new IllegalArgumentException("Le titre doit être spécifié.");
        } else if (defi.getDuree()==null) {
            throw new IllegalArgumentException("La durée doit être spécifiée.");
        } else if (defi.getPointsAGagner()==null || defi.getPointsAGagner()<=0) {
            throw new IllegalArgumentException("Les points à gagner doivent être >= 0.");
        } else if(defi.getDateFin()==null){
            throw new IllegalArgumentException("La date de fin doit être spécifiée.");
        }
        DefiDeProcrastination savedDefi = new DefiDeProcrastination();
        savedDefi.setDateDebut(defi.getDateDebut());
        savedDefi.setDateFin(defi.getDateFin());
        savedDefi.setTitre(defi.getTitre());
        savedDefi.setDuree(defi.getDuree());
        savedDefi.setCreateur(utilisateurCourant);
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