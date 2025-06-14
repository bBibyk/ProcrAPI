package org.bem.procrapi.services;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryParticipationDefi;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutDefi;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceParticipationDefi {
    private final RepositoryParticipationDefi participationRepo;
    private final RepositoryDefiDeProcrastination defiRepo;
    private final ServiceUtilisateur utilisateurService;
    private final RepositoryUtilisateur utilisateurRepo;

    @Autowired
    public ServiceParticipationDefi(RepositoryParticipationDefi participationRepo,
                                    RepositoryDefiDeProcrastination defiRepo, ServiceUtilisateur utilisateurService, RepositoryUtilisateur utilisateurRepo) {
        this.participationRepo = participationRepo;
        this.defiRepo = defiRepo;
        this.utilisateurService = utilisateurService;
        this.utilisateurRepo = utilisateurRepo;
    }

    public ParticipationDefi create(ParticipationDefi partcipation) {
        Utilisateur utilisateurCourant = UtilisateurHolder.getCurrentUser();

        if (utilisateurCourant == null) {
            throw new IllegalArgumentException("Utilisateur inexistant.");
        } else if (utilisateurCourant.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE) {
            throw new IllegalArgumentException("Seuls les Procrastinateurs en Herbe peuvent participer à un défi.");
        }

        Utilisateur utilisateur=utilisateurRepo.findById(utilisateurCourant.getId()).get();

        if (partcipation.getDefi() == null || partcipation.getDefi().getId() == null) {
            throw new IllegalArgumentException("Défi non spécifié.");
        }

        DefiDeProcrastination defi = defiRepo.findById(partcipation.getDefi().getId())
                .orElseThrow(() -> new IllegalArgumentException("Défi introuvable."));

        /* Il existe des contraintes imposées dans les règles :
         * Un utilisateur ne peut participer simultanément qu’à 3 défis maximum
         * Un défi ne peut accueillir plus de 5 participants
         */

        // Vérifie les 3 participations simultanées
        long defisEnCours = utilisateur.getParticipations().size();
        if (defisEnCours >= 3) {
            throw new IllegalArgumentException("Vous participez déjà à 3 défis.");
        }

        // Vérifie les 5 participants max
        long nbParticipants = partcipation.getDefi().getParticipations().size();
        if (nbParticipants >= 5) {
            throw new IllegalArgumentException("Ce défi a atteint le nombre maximal de participants (5).");
        }

        ParticipationDefi nouvelleParticipation = new ParticipationDefi();
        nouvelleParticipation.setUtilisateur(utilisateurCourant);
        nouvelleParticipation.setDefi(defi);
        nouvelleParticipation.setDateInscription(LocalDate.now());
        // Ici peut être il faut vérifier si c'est null mais si non le cas par défaut ?
        nouvelleParticipation.setStatut(StatutParticipation.INSCRIT);

        //TODO C'est quand qu'on donne les points à l'utilisateur ?
        if(partcipation.getDefi().getStatut()== StatutDefi.TERMINE){
            nouvelleParticipation.setPoints(partcipation.getDefi().getPointsAGagner());
            utilisateurService.attribuerPoints(utilisateur, nouvelleParticipation.getPoints());
        }

        return participationRepo.save(nouvelleParticipation);
    }
}
