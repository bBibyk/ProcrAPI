package org.bem.procrapi.services;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.ConfrontationPiege;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryConfrontationPiege;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ServiceConfrontationPiege {

    private final RepositoryConfrontationPiege confrontationPiegeRepo;
    private final RepositoryPiegeDeProductivite piegeRepo;

    @Autowired
    public ServiceConfrontationPiege(RepositoryConfrontationPiege confrontationPiegeRepo,
                                     RepositoryPiegeDeProductivite piegeRepo) {
        this.confrontationPiegeRepo = confrontationPiegeRepo;
        this.piegeRepo = piegeRepo;
    }

    public ConfrontationPiege create(ConfrontationPiege confrontation) {
        Utilisateur utilisateur = UtilisateurHolder.getCurrentUser();
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        }
        if (utilisateur.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE ){
            throw new IllegalArgumentException("Vous n'avez pas les droits pour créer une confrontation.");
        }

        if (confrontation == null) {
            throw new IllegalArgumentException("Confrontation invalide.");
        }
        // TODO vérifier si la date n'est pas dans le passé sinon erreur
        if (confrontation.getPiege() == null || confrontation.getPiege().getId() == null) {
            throw new IllegalArgumentException("Le piège doit être spécifi.");
        }

        PiegeDeProductivite piege = piegeRepo.findById(confrontation.getPiege().getId())
                .orElseThrow(() -> new IllegalArgumentException("Piège introuvable."));


        confrontation.setUtilisateur(utilisateur);
        confrontation.setPiege(piege);

        // Date confrontation par défaut à aujourd'hui si non précisée TODO tu peux directement la set dans l'entité, comme ça on s'embete pas
        if (confrontation.getDateConfrontation() == null) {
            confrontation.setDateConfrontation(new Date());
        }

        if (confrontation.getResultat() == null) {
            throw new IllegalArgumentException("Le résultat doit être précisé");
        }
        // Points par défaut à 0 si null TODO pas du tout, c'est +/- 50 points en fonction de si succès ou défaite
        if (confrontation.getPoints() == null) {
            confrontation.setPoints(0);
        }

        return confrontationPiegeRepo.save(confrontation);
    }

}
