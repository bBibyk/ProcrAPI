package org.bem.procrapi.services;

import org.bem.procrapi.entities.*;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.TypePiege;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePiegeDeProductivite {

    private final RepositoryPiegeDeProductivite piegeRepo;
    private final ServiceUtilisateur utilisateurService;
    private final RepositoryRecompense repositoryRecompense;


    @Autowired
    public ServicePiegeDeProductivite(RepositoryPiegeDeProductivite repositoryPiege,
                                      ServiceUtilisateur utilisateurService, RepositoryRecompense repositoryRecompense){
        this.piegeRepo = repositoryPiege;
        this.utilisateurService = utilisateurService;
        this.repositoryRecompense = repositoryRecompense;
    }

    public PiegeDeProductivite create(String titre,
                                      TypePiege type,
                                      Integer difficulte,
                                      String description,
                                      String titreRecompense,
                                      String consequence) {
        Utilisateur currentUser = utilisateurService.getUtilisateurCourant();

        if (currentUser == null) {
            throw new ServiceValidationException("Utilisateur non authentifié.");
        } else if (currentUser.getRole() != RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI) {
            throw new ServiceValidationException("Seuls les Anti-Procrastinateurs Repentis peuvent créer un piège.");
        }else if (titre == null ) {
            throw new ServiceValidationException("Titre est obligatoire.");
        } else if (piegeRepo.findByTitre(titre).isPresent()) {
            throw new ServiceValidationException("Ce piège existe déjà.");
        } else if (type == null) {
            throw new ServiceValidationException("Type est obligatoire.");
        }else if (difficulte == null || (difficulte < 1 || difficulte > 5) ) {
            throw new ServiceValidationException("Difficulté invalide.");
        }

        PiegeDeProductivite newPiege = new PiegeDeProductivite();
        newPiege.setTitre(titre);
        newPiege.setType(type);
        if(description != null){
            newPiege.setDescription(description);
        }
        newPiege.setDifficulte(difficulte);
        if(titreRecompense==null){
            throw new ServiceValidationException("Recompense non valide.");
        }
        Recompense recompenseFull = repositoryRecompense.findByTitre(titreRecompense)
                .orElseThrow(()-> new ServiceValidationException("Recompense non trouvée."));
        newPiege.setRecompense(recompenseFull);

        if(consequence != null){
            newPiege.setConsequence(consequence);
        }
        newPiege.setCreateur(currentUser);

        return piegeRepo.save(newPiege);
    }

    /**
     * Récupère la liste de tous les pièges de productivité enregistrées en base de données.
     *
     * @return une liste de {@link PiegeDeProductivite} représentant toutes les entités stockées.
     */
    public List<PiegeDeProductivite> getAll() {
        return piegeRepo.findAll();
    }
}
