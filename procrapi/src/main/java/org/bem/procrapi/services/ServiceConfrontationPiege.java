package org.bem.procrapi.services;

import org.bem.procrapi.entities.*;
import org.bem.procrapi.repositories.RepositoryAttributionRecompense;
import org.bem.procrapi.repositories.RepositoryConfrontationPiege;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ServiceConfrontationPiege {

    private final RepositoryConfrontationPiege confrontationPiegeRepo;
    private final RepositoryPiegeDeProductivite piegeRepo;
    private final ServiceUtilisateur utilisateurService;
    private final ServiceRecompense serviceRecompense;
    private final ServiceAttributionRecompense serviceAttributionRecompense;
    private final RepositoryAttributionRecompense repositoryAttributionRecompense;

    @Autowired
    public ServiceConfrontationPiege(RepositoryConfrontationPiege confrontationPiegeRepo,
                                     RepositoryPiegeDeProductivite piegeRepo,
                                     ServiceUtilisateur utilisateurService, ServiceRecompense serviceRecompense, ServiceAttributionRecompense serviceAttributionRecompense, RepositoryAttributionRecompense repositoryAttributionRecompense) {
        this.confrontationPiegeRepo = confrontationPiegeRepo;
        this.piegeRepo = piegeRepo;
        this.utilisateurService = utilisateurService;
        this.serviceRecompense = serviceRecompense;
        this.serviceAttributionRecompense = serviceAttributionRecompense;
        this.repositoryAttributionRecompense = repositoryAttributionRecompense;
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

                // TODO changer avec le code en dessous après Dali fait les modifs
                Recompense recompense = new Recompense();
                recompense.setType(TypeRecompense.BADGE);
                recompense.setTitre("Procrastinateur en Danger");
                AttributionRecompense attribution = new AttributionRecompense();
                attribution.setUtilisateur(utilisateur);
                attribution.setRecompense(recompense);
                attribution.setDateObtention(LocalDate.now());
                attribution.setDateExpiration(LocalDate.now().plusDays(7));
                attribution.setContexteAttribution("Piège de productivité raté");
                attribution.setStatut(StatutRecompense.ACTIF);

                /* Je veux bien passer par le service mais pour ça,
                il faut que Dali fasse ses modifs pour passer en paramètre date d'expiration */

                //serviceAttributionRecompense.attribuerRecompense(utilisateur, recompense,"Piège de productivité raté");
                //attribution.setDateExpiration(LocalDate.now().plusDays(7));
            }
        }

        utilisateur.setPointsAccumules(confrontationSauvegardee.getPoints());
        return confrontationPiegeRepo.save(confrontationSauvegardee);
    }
}
