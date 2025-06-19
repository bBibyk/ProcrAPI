package org.bem.procrapi.services;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.entities.ConfrontationPiege;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryConfrontationPiege;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceConfrontationPiege {

    private final RepositoryConfrontationPiege confrontationPiegeRepo;
    private final RepositoryPiegeDeProductivite piegeRepo;
    private final ServiceUtilisateur utilisateurService;
    private final ServiceAttributionRecompense serviceAttributionRecompense;

    @Autowired
    public ServiceConfrontationPiege(RepositoryConfrontationPiege confrontationPiegeRepo,
                                     RepositoryPiegeDeProductivite piegeRepo,
                                     ServiceUtilisateur utilisateurService,
                                     ServiceAttributionRecompense serviceAttributionRecompense) {
        this.confrontationPiegeRepo = confrontationPiegeRepo;
        this.piegeRepo = piegeRepo;
        this.utilisateurService = utilisateurService;
        this.serviceAttributionRecompense = serviceAttributionRecompense;
    }

    public ConfrontationPiege create(String titrePiege,
                                     ResultatConfrontationPiege resultat) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurCourant();
        ConfrontationPiege confrontationSauvegardee=new ConfrontationPiege();

        if (utilisateur.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE ){
            throw new ServiceValidationException("Vous n'avez pas les droits pour créer une confrontation.");
        }

        if (titrePiege == null) {
            throw new ServiceValidationException("Le piège doit être spécifié.");
        }
        PiegeDeProductivite piegeFull = piegeRepo.findByTitre(titrePiege)
                .orElseThrow(() -> new ServiceValidationException("Piège introuvable."));

        if (resultat == null) {
            throw new ServiceValidationException("Le résultat doit être précisé");
        }

        confrontationSauvegardee.setUtilisateur(utilisateur);
        confrontationSauvegardee.setPiege(piegeFull);
        confrontationSauvegardee.setDateConfrontation(LocalDate.now());
        switch (resultat){
            case SUCCES-> {
                confrontationSauvegardee.setPoints(50);
                serviceAttributionRecompense.create(
                        utilisateur.getEmail(),
                        piegeFull.getRecompense().getTitre(),
                        "Piège de productivité évité",
                        LocalDate.now().plusDays(30)); // arbitrairement on a décidé que la récompense est attribuée pour 1 mois
            }
            case ECHEC -> {
                confrontationSauvegardee.setPoints(-50);

                /* Application de la règle métier concernant les pièges de productivité :
                 * cas d'échec utilisateur => utilisateur reçoit le badge "Procrastinateur en Danger"
                 * pour une durée d'une semaine
                 */
                serviceAttributionRecompense.create(
                        utilisateur.getEmail(),
                        "Procrastinateur en Danger",
                        "Piège de productivité raté",
                        LocalDate.now().plusDays(7));
            }
        }

        utilisateur.setPointsAccumules(confrontationSauvegardee.getPoints());
        return confrontationPiegeRepo.save(confrontationSauvegardee);
    }

    /**
     * Récupère la liste de toutes les confrontations piège enregistrées en base de données.
     *
     * @return une liste de {@link ConfrontationPiege} représentant toutes les entités stockées.
     */
    public List<ConfrontationPiege> getAll() {
        return confrontationPiegeRepo.findAll();
    }
}
