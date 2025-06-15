package org.bem.procrapi.services;

import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.TypePiege;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PiegeDeProductivite create(String titre,
                                      TypePiege type,
                                      Integer difficulte,
                                      String description) {
        Utilisateur currentUser = utilisateurService.getUtilisateurCourant();

        if (currentUser == null) {
            throw new ServiceValidationException("Utilisateur non authentifié.");
        } else if (currentUser.getRole() != RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI) {
            throw new ServiceValidationException("Seuls les Anti-Procrastinateurs Repentis peuvent créer un piège.");
        }else if (titre == null ) {
            throw new ServiceValidationException("Titre obligatoire.");
        }else if (type == null) {
            throw new ServiceValidationException("Type obligatoire.");
        }else if (difficulte == null || (difficulte < 1 || difficulte > 5) ) {
            throw new ServiceValidationException("Difficulté invalide.");
        }

        PiegeDeProductivite newPiege = new PiegeDeProductivite();
        newPiege.setTitre(titre);
        newPiege.setType(type);
        newPiege.setDescription(description);
        newPiege.setDifficulte(difficulte); //TODO si description null ??
        // TODO recompense ? conséquences ?
        newPiege.setCreateur(currentUser);

        return piegeRepo.save(newPiege);
    }

}
