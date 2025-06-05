package org.bem.procrapi.services;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryParticipationDefi;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ServiceParticipationDefi {
    private final RepositoryParticipationDefi participationRepo;
    private final RepositoryUtilisateur userRepo;
    private final RepositoryDefiDeProcrastination defiRepo;

    @Autowired
    public ServiceParticipationDefi(RepositoryParticipationDefi participationRepo,
                                    RepositoryUtilisateur userRepo,
                                    RepositoryDefiDeProcrastination defiRepo) {
        this.participationRepo = participationRepo;
        this.userRepo = userRepo;
        this.defiRepo = defiRepo;
    }

    public ParticipationDefi create(Long idUtilisateur, Long idDefi) {
        Utilisateur utilisateur = userRepo.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        DefiDeProcrastination defi = defiRepo.findById(idDefi)
                .orElseThrow(() -> new IllegalArgumentException("Défi introuvable"));

        /* Il existe des contraintes imposées dans les règles :
        * Un utilisateur ne peut participer simultanément qu’à 3 défis maximum
        * Un défi ne peut accueillir plus de 5 participants
        */

        // Vérifie les 3 participations simultanées
        long nbDefis = participationRepo.countByUtilisateurAndStatutIn(utilisateur,
                List.of(StatutParticipation.INSCRIT, StatutParticipation.EN_COURS)
        );
        if (nbDefis >= 3) {
            throw new IllegalArgumentException("L'utilisateur ne peut participer simultanément qu’à 3 défis maximum.");
        }
        // Vérifie les 5 participants max
        long nbParticipants = participationRepo.countByDefi(defi);
        if (nbParticipants >= 5) {
            throw new IllegalArgumentException("Ce défi a déjà 5 participants.");
        }

        ParticipationDefi participation = new ParticipationDefi();
        participation.setUtilisateur(utilisateur);
        participation.setDefi(defi);
        participation.setDateInscription(new Date()); // Par défaut
        participation.setStatut(StatutParticipation.INSCRIT);
        participation.setPoints(0); // Par défaut

        return participationRepo.save(participation);
    }

}
