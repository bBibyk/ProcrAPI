package org.bem.procrapi.services;

import org.bem.procrapi.entities.*;
import org.bem.procrapi.repositories.RepositoryAttributionRecompense;
import org.bem.procrapi.repositories.RepositoryConfrontationPiege;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServiceConfrontationPiege {

    private final RepositoryConfrontationPiege confrontationPiegeRepo;
    private final RepositoryPiegeDeProductivite piegeRepo;
    private final ServiceUtilisateur utilisateurService;
    private final ServiceAttributionRecompense serviceAttributionRecompense;

    @Autowired
    public ServiceConfrontationPiege(RepositoryConfrontationPiege confrontationPiegeRepo,
                                     RepositoryPiegeDeProductivite piegeRepo,
                                     ServiceUtilisateur utilisateurService, ServiceRecompense serviceRecompense, ServiceAttributionRecompense serviceAttributionRecompense, RepositoryAttributionRecompense repositoryAttributionRecompense) {
        this.confrontationPiegeRepo = confrontationPiegeRepo;
        this.piegeRepo = piegeRepo;
        this.utilisateurService = utilisateurService;
        this.serviceAttributionRecompense = serviceAttributionRecompense;
    }

    public ConfrontationPiege create(ConfrontationPiege confrontation) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurCourant();
        ConfrontationPiege confrontationSauvegardee=new ConfrontationPiege();

        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        }
        if (utilisateur.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE ){
            throw new IllegalArgumentException("Vous n'avez pas les droits pour créer une confrontation.");
        }

        if (confrontation == null) {
            throw new IllegalArgumentException("Confrontation invalide.");
        }

        if (confrontation.getPiege() == null || confrontation.getPiege().getId() == null) {
            throw new IllegalArgumentException("Le piège doit être spécifié.");
        }

        PiegeDeProductivite piege = piegeRepo.findById(confrontation.getPiege().getId())
                .orElseThrow(() -> new IllegalArgumentException("Piège introuvable."));

        if (confrontation.getResultat() == null) {
            throw new IllegalArgumentException("Le résultat doit être précisé");
        }

        if (confrontation.getPoints() == null) {
            throw new IllegalArgumentException("Les points doivent être précisés.");
        }

        confrontationSauvegardee.setUtilisateur(utilisateur);
        confrontationSauvegardee.setPiege(piege);
        confrontationSauvegardee.setDateConfrontation(LocalDate.now());
        switch (confrontation.getResultat()){
            case SUCCES-> {
                confrontationSauvegardee.setPoints(50);
            }
            case ECHEC -> {
                confrontationSauvegardee.setPoints(-50);

                /* Application de la règle métier concernant les pièges de productivité :
                 * cas d'échec utilisateur => utilisateur reçoit le badge "Procrastinateur en Danger"
                 * pour une durée d'une semaine
                 */
                Recompense recompense = new Recompense();
                recompense.setType(TypeRecompense.BADGE);
                recompense.setTitre("Procrastinateur en Danger");
                serviceAttributionRecompense.attribuerRecompense(utilisateur, recompense,"Piège de productivité raté", LocalDate.now().plusDays(7));
            }
        }

        utilisateur.setPointsAccumules(confrontationSauvegardee.getPoints());
        return confrontationPiegeRepo.save(confrontationSauvegardee);
    }
}
