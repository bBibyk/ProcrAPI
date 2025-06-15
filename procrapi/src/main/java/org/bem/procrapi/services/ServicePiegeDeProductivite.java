package org.bem.procrapi.services;

import org.bem.procrapi.components.authentication.EmailHolder;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutPiege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServicePiegeDeProductivite {

    private final RepositoryPiegeDeProductivite piegeRepo;
    private final ServiceUtilisateur utilisateurService;


    @Autowired
    public ServicePiegeDeProductivite(RepositoryPiegeDeProductivite repositoryPiege,
                                      ServiceUtilisateur utilisateurService){
        this.piegeRepo = repositoryPiege;
        this.utilisateurService = utilisateurService;
    }

    public PiegeDeProductivite create(PiegeDeProductivite piege) {
        Utilisateur currentUser = utilisateurService.getUtilisateurCourant();

        if (currentUser == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        } else if (currentUser.getRole() != RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI) {
            throw new IllegalArgumentException("Seuls les Anti-Procrastinateurs Repentis peuvent créer un piège.");
        }else if (piege.getTitre() == null ) {
            throw new IllegalArgumentException("Titre obligatoire.");
        }else if (piege.getType() == null) {
            throw new IllegalArgumentException("Type obligatoire.");
        }else if (piege.getDifficulte() == null || (piege.getDifficulte() < 1 || piege.getDifficulte() > 5) ) {
            throw new IllegalArgumentException("Difficulté invalide.");
        }

        PiegeDeProductivite newPiege = new PiegeDeProductivite();
        newPiege.setTitre(piege.getTitre());
        newPiege.setType(piege.getType());
        newPiege.setDescription(piege.getDescription());
        newPiege.setDifficulte(piege.getDifficulte());
        newPiege.setDateCreation(LocalDate.now()); // Valeur par défaut
        newPiege.setStatut(StatutPiege.ACTIF); // Valeur par défaut
        newPiege.setCreateur(currentUser);

        return piegeRepo.save(newPiege);
    }

}
