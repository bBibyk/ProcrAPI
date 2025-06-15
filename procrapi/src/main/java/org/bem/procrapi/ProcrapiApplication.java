package org.bem.procrapi;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceRecompense;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class ProcrapiApplication implements CommandLineRunner {

    @Autowired
    private ServiceRecompense serviceRecompense;
    @Autowired
    private RepositoryRecompense repositoryRecompense;

    public static void main(String[] args) {
        SpringApplication.run(ProcrapiApplication.class, args);
    }

    @Autowired
    RepositoryUtilisateur repositoryUtilisateur;

    @Override
    public void run(String... args) throws Exception {
        String gestionnaire_mail = "el.flemardo@miage.fr";
        if (repositoryUtilisateur.findByEmail(gestionnaire_mail).isEmpty()) {
            Utilisateur gestionnaireDuTempsPerdu = new Utilisateur();
            gestionnaireDuTempsPerdu.setRole(RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU);
            gestionnaireDuTempsPerdu.setNiveau(NiveauProcrastination.EXPERT);
            gestionnaireDuTempsPerdu.setPointsAccumules(2000);
            gestionnaireDuTempsPerdu.setPseudo("LaFl3ww");
            gestionnaireDuTempsPerdu.setEmail(gestionnaire_mail);
            repositoryUtilisateur.save(gestionnaireDuTempsPerdu);
        }

        // Création du badge procrastinateur en danger, vu qu'il est par défaut dans le programme (règle métier)
        //TODO if existe déjà ?
        Recompense recompense = new Recompense();
        recompense.setTitre("Procrastinateur en Danger");
        recompense.setConditionsObtention("Échouer à un piège de productivité.");
        recompense.setType(TypeRecompense.BADGE);
        repositoryRecompense.save(recompense);

        System.out.println("Database initialized!");
    }
}
