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

import java.time.LocalDate;
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

    public ParticipationDefi create(ParticipationDefi partcipation) {
        Utilisateur currentUser = UtilisateurHolder.getCurrentUser();

        if (currentUser == null) {
            throw new IllegalArgumentException("Utilisateur non authentifié.");
        } else if (currentUser.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE) {
            throw new IllegalArgumentException("Seuls les Procrastinateurs en Herbe peuvent participer à un défi.");
        }

        if (partcipation == null || partcipation.getDefi() == null || partcipation.getDefi().getId() == null) {
            throw new IllegalArgumentException("Défi non spécifié.");
        }

        DefiDeProcrastination defi = defiRepo.findById(partcipation.getDefi().getId())
                .orElseThrow(() -> new IllegalArgumentException("Défi introuvable."));

        /* Il existe des contraintes imposées dans les règles :
         * Un utilisateur ne peut participer simultanément qu’à 3 défis maximum
         * Un défi ne peut accueillir plus de 5 participants
         */

        // Vérifie les 3 participations simultanées
        // TODO très gros overkill (pourquoi solliciter la BD ?), pas besoin, il suffit de load l'utilisateur et de redared son attribut défis
        long defisEnCours = participationRepo.countByUtilisateurAndStatutIn(
                currentUser, List.of(StatutParticipation.INSCRIT, StatutParticipation.EN_COURS)
        );
        if (defisEnCours >= 3) {
            throw new IllegalArgumentException("Vous participez déjà à 3 défis.");
        }

        // Vérifie les 5 participants max
        // TODO Tout pareil que pour l'utilisateur
        long nbParticipants = participationRepo.countByDefi(defi);
        if (nbParticipants >= 5) {
            throw new IllegalArgumentException("Ce défi a atteint le nombre maximal de participants (5).");
        }
        // TODO C'est quand qu'on donne les points à l'utilisateur ?

        ParticipationDefi nouvelleParticipation = new ParticipationDefi();
        nouvelleParticipation.setUtilisateur(currentUser);
        nouvelleParticipation.setDefi(defi);
        nouvelleParticipation.setDateInscription(LocalDate.now());
        nouvelleParticipation.setStatut(StatutParticipation.INSCRIT);
        nouvelleParticipation.setPoints(0); //TODO Pourquoi ?

        return participationRepo.save(nouvelleParticipation);
    }
}
