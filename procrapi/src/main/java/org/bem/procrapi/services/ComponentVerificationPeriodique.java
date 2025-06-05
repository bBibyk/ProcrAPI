package org.bem.procrapi.services;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComponentVerificationPeriodique {

    @Scheduled(fixedRate = 60000)
    public void createUser(){
        System.out.println("works");
    }

    /*
    // Exemple : toutes les 24h (86_400_000 ms)
    @Scheduled(fixedRate = 86400000)
    public void verifierConfrontationsMensuelles() {
        System.out.println("Vérification des confrontations mensuelles...");

        List<Utilisateur> procrastinateurs = utilisateurRepo.findByRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);

        Calendar debutMois = Calendar.getInstance();
        debutMois.set(Calendar.DAY_OF_MONTH, 1);
        debutMois.set(Calendar.HOUR_OF_DAY, 0);
        debutMois.set(Calendar.MINUTE, 0);
        debutMois.set(Calendar.SECOND, 0);
        debutMois.set(Calendar.MILLISECOND, 0);

        Calendar finMois = (Calendar) debutMois.clone();
        finMois.add(Calendar.MONTH, 1);

        for (Utilisateur utilisateur : procrastinateurs) {
            List<ConfrontationPiege> confrontations =
                    confrontationRepo.findByUtilisateurAndDateConfrontationBetween(
                            utilisateur, debutMois.getTime(), finMois.getTime());

            if (confrontations.isEmpty()) {
                System.out.println("Rappel envoyé à : " + utilisateur.getEmail() +
                        " → Aucune confrontation enregistrée ce mois-ci !");
            }
        }
    }*/
}
