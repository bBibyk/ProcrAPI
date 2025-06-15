package org.bem.procrapi.services;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryParticipationDefi;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutDefi;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public ParticipationDefi create(ParticipationDefi partcipation) {
        Utilisateur utilisateurCourant = utilisateurService.getUtilisateurCourant();

        if (utilisateurCourant.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE) {
            throw new ServiceValidationException("Seuls les Procrastinateurs en Herbe peuvent participer à un défi.");
        }

        if (partcipation.getDefi() == null || partcipation.getDefi().getId() == null) {
            throw new ServiceValidationException("Défi non spécifié.");
        }

        DefiDeProcrastination defi = defiRepo.findById(partcipation.getDefi().getId())
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
        int nbParticipants = partcipation.getDefi().getParticipations().size();
        if (nbParticipants >= 5) {
            throw new ServiceValidationException("Ce défi a atteint le nombre maximal de participants (5).");
        }

        ParticipationDefi nouvelleParticipation = new ParticipationDefi();
        nouvelleParticipation.setUtilisateur(utilisateurCourant);
        nouvelleParticipation.setDefi(defi);
        nouvelleParticipation.setDateInscription(LocalDate.now());
        nouvelleParticipation.setStatut(StatutParticipation.INSCRIT);

        /* Mise à jour des points gagnés par la participation
        et attribution des points à l'utilisateur à la fin du défi */
        if(partcipation.getDefi().getStatut()== StatutDefi.TERMINE){
            nouvelleParticipation.setPoints(partcipation.getDefi().getPointsAGagner());
            utilisateurService.attribuerPoints(utilisateurCourant, nouvelleParticipation.getPoints());
        }

        return participationRepo.save(nouvelleParticipation);
    }
}
