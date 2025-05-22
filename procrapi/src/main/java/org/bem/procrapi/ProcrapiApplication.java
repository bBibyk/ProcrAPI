package org.bem.procrapi;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class ProcrapiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProcrapiApplication.class, args);
    }

    @Autowired
    RepositoryUtilisateur repositoryUtilisateur;

    @Override
    public void run(String... args) throws Exception {
        String gestionnaire_mail = "dali.mabrouk@miage.fr";
        if (repositoryUtilisateur.findByEmail(gestionnaire_mail).isEmpty()) {
            Utilisateur gestionnaireDuTempsPerdu = new Utilisateur();
            gestionnaireDuTempsPerdu.setRole(RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU);
            gestionnaireDuTempsPerdu.setDateInscription(new Date());
            gestionnaireDuTempsPerdu.setEmail(gestionnaire_mail);
            repositoryUtilisateur.save(gestionnaireDuTempsPerdu);
        }

        System.out.println("Database initialized!");
    }
}
