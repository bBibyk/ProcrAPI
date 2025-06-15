package org.bem.procrapi.services;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.utilities.enumerations.DifficulteDefi;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
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


    public DefiDeProcrastination create(LocalDate dateDebut,
                                        String titre,
                                        Integer duree,
                                        Integer pointsAGagner,
                                        String description,
                                        DifficulteDefi difficulte) {
        Utilisateur utilisateurCourant = utilisateurService.getUtilisateurCourant();
        if (utilisateurCourant.getRole() != RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new ServiceValidationException("Seul le gestionnaire du temps perdu peut créer des défis.");
        } else if (titre==null) {
            throw new ServiceValidationException("Le titre doit être spécifié.");
        } else if (repositoryDefiDeProcrastination.findByTitre(titre).isPresent()) {
            throw new ServiceValidationException("Ce défi existe déjà.");
        } else if (dateDebut==null) {
            throw new ServiceValidationException("La date de début doit être spécifiée.");
        } else if (duree==null) {
            throw new ServiceValidationException("La durée doit être spécifiée.");
        } else if (duree<=0) {
            throw new ServiceValidationException("La durée doit être positive.");
        } else if (!dateDebut.isAfter(LocalDate.now())) {
            throw new ServiceValidationException("La date de début doit être postérieure à la date d'ajourd'hui.");
        } else if (pointsAGagner==null || pointsAGagner<=0) {
            throw new ServiceValidationException("Les points à gagner doivent être >= 0.");
        }

        LocalDate dateFin = LocalDate.now().plusDays(duree-1);

        DefiDeProcrastination savedDefi = new DefiDeProcrastination();
        savedDefi.setDateDebut(dateDebut);
        savedDefi.setDateFin(dateFin);
        savedDefi.setTitre(titre);
        savedDefi.setDuree(duree);
        savedDefi.setCreateur(utilisateurCourant);
        savedDefi.setPointsAGagner(pointsAGagner);
        if(description!=null){
            savedDefi.setDescription(description);
        }
        if(difficulte!=null){
            savedDefi.setDifficulte(difficulte);
        }
        return repositoryDefiDeProcrastination.save(savedDefi);
    }
}