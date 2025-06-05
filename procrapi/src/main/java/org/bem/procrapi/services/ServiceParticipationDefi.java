package org.bem.procrapi.services;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryParticipationDefi;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ServiceParticipationDefi {
    private final RepositoryParticipationDefi participationRepo;
    private final RepositoryDefiDeProcrastination defiRepo;

    @Autowired
    public ServiceParticipationDefi(RepositoryParticipationDefi participationRepo,
                                    RepositoryDefiDeProcrastination defiRepo) {
        this.participationRepo = participationRepo;
        this.defiRepo = defiRepo;
    }

    public ParticipationDefi create(ParticipationDefi participationDefi) {
        Utilisateur utilisateur=UtilisateurHolder.getCurrentUser();
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur introuvable");
        }
        if (utilisateur.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE) {
            throw new IllegalArgumentException("Seuls les procrastonateur en herbe peuvent participer à des défis.");
        }
        if (participationDefi == null || participationDefi.getDefi() == null) {
            throw new IllegalArgumentException("Le défi à rejoindre doit être spécifié.");
        }

        DefiDeProcrastination defi = defiRepo.findById(participationDefi.getDefi().getId())
                .orElseThrow(() -> new IllegalArgumentException("Défi introuvable."));

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
        long nbParticipants = participationRepo.countByDefi(participationDefi.getDefi());
        if (nbParticipants >= 5) {
            throw new IllegalArgumentException("Ce défi a déjà 5 participants.");
        }

        ParticipationDefi participation = new ParticipationDefi();
        participation.setUtilisateur(utilisateur);
        participation.setDefi(participationDefi.getDefi());
        participation.setDateInscription(new Date()); // Par défaut
        participation.setStatut(StatutParticipation.INSCRIT);
        participation.setPoints(0); // Par défaut

        return participationRepo.save(participation);
    }

}
