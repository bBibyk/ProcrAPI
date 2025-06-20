package org.bem.procrapi.services;

import org.bem.procrapi.entities.*;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryParticipationDefi;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutDefi;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceParticipationDefi {
    private final RepositoryParticipationDefi participationRepo;
    private final RepositoryDefiDeProcrastination defiRepo;
    private final ServiceUtilisateur utilisateurService;

    @Autowired
    public ServiceParticipationDefi(RepositoryParticipationDefi participationRepo,
                                    RepositoryDefiDeProcrastination defiRepo, ServiceUtilisateur utilisateurService) {
        this.participationRepo = participationRepo;
        this.defiRepo = defiRepo;
        this.utilisateurService = utilisateurService;
    }

    public ParticipationDefi create(String titreDefi) {
        Utilisateur utilisateurCourant = utilisateurService.getUtilisateurCourant();

        if (utilisateurCourant.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE) {
            throw new ServiceValidationException("Seuls les Procrastinateurs en Herbe peuvent participer à un défi.");
        }

        if (titreDefi == null) {
            throw new ServiceValidationException("Défi non spécifié.");
        }

        DefiDeProcrastination defi = defiRepo.findByTitre(titreDefi)
                .orElseThrow(() -> new ServiceValidationException("Le défis spécifié est introuvable."));

        /* Il existe des contraintes imposées dans les règles :
         * Un utilisateur ne peut participer simultanément qu’à 3 défis maximum
         * Un défi ne peut accueillir plus de 5 participants
         */

        // Vérifie les 3 participations simultanées
        int defisEnCours = (int) utilisateurCourant.getParticipations().stream()
                .filter(p -> p.getStatut() == StatutParticipation.EN_COURS || p.getStatut() == StatutParticipation.INSCRIT)
                .count();
        if (defisEnCours >= 3) {
            throw new ServiceValidationException("Vous participez déjà à 3 défis.");
        }

        // Vérifie les 5 participants max
        int nbParticipants = defi.getParticipations().size();
        if (nbParticipants >= 5) {
            throw new ServiceValidationException("Ce défi a atteint le nombre maximal de participants (5).");
        }

        ParticipationDefi nouvelleParticipation = new ParticipationDefi();
        nouvelleParticipation.setUtilisateur(utilisateurCourant);
        nouvelleParticipation.setDefi(defi);
        nouvelleParticipation.setDateInscription(LocalDate.now());
        nouvelleParticipation.setStatut(StatutParticipation.INSCRIT);

        return participationRepo.save(nouvelleParticipation);
    }

    /**
     * Récupère la liste de toutes les participation defi enregistrées en base de données.
     *
     * @return une liste de {@link ParticipationDefi} représentant toutes les entités stockées.
     */
    public List<ParticipationDefi> getAll() {
        return participationRepo.findAll();
    }
}
