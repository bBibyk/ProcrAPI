package org.bem.procrapi;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceRecompense;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
// CommandLineRunner qui permet d'initialiser des entités nécessaires dans notre BD
public class ProcrapiApplication implements CommandLineRunner {

    @Autowired
    private RepositoryRecompense repositoryRecompense;
    @Autowired
    private RepositoryExcuseCreative repositoryExcuseCreative;
    @Autowired
    private RepositoryUtilisateur repositoryUtilisateur;

    public static void main(String[] args) {
        SpringApplication.run(ProcrapiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Création du Gestionnaire de Temps Perdu au cas où la BD a été réinitialisée
        String gestionnaireMail = "professeur.flemardo@irit.fr";
        if (repositoryUtilisateur.findByEmail(gestionnaireMail).isEmpty()) {
            Utilisateur gestionnaireDuTempsPerdu = new Utilisateur();
            gestionnaireDuTempsPerdu.setRole(RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU);
            // On lui donne du niveau pour pouvoir attribuer l'OR à qqn
            gestionnaireDuTempsPerdu.setNiveau(NiveauProcrastination.EXPERT);
            gestionnaireDuTempsPerdu.setPointsAccumules(2000);
            gestionnaireDuTempsPerdu.setPseudo("LaFl3ww");
            gestionnaireDuTempsPerdu.setEmail(gestionnaireMail);
            repositoryUtilisateur.save(gestionnaireDuTempsPerdu);
        }

        // Création du badge procrastinateur en danger, vu qu'il est par défaut dans le programme (règle métier)
        String titreRecompense = "Procrastinateur en Danger";
        if(repositoryRecompense.findByTitre(titreRecompense).isEmpty()){
            Recompense recompense = new Recompense();
            recompense.setTitre(titreRecompense);
            recompense.setConditionsObtention("Échouer à un piège de productivité.");
            recompense.setType(TypeRecompense.BADGE);
            repositoryRecompense.save(recompense);
        }

        // Création d'une excuse pour pouvoir faire la présentation le jour de l'exam
        String texteExcuse = "Excuse ultime de je n'avais pas le temps";
        if(repositoryExcuseCreative.findByTexte(texteExcuse).isEmpty()){
            ExcuseCreative excuse = new ExcuseCreative();
            excuse.setTexte(texteExcuse);
            excuse.setStatut(StatutExcuse.APPROUVEE);
            excuse.setCreateur(repositoryUtilisateur.findByEmail(gestionnaireMail).get());
            repositoryExcuseCreative.save(excuse);
        }

        System.out.println("Database initialized!");
    }
}
