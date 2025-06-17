package org.bem.procrapi;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProcrapiApplicationTest {

    @Autowired private RepositoryUtilisateur utilisateurRepo;
    @Autowired private RepositoryRecompense recompenseRepo;
    @Autowired private RepositoryExcuseCreative excuseRepo;

    @Test
    public void testInitialDataCreated() {
        // Vérifie le gestionnaire
        Utilisateur gestionnaire = utilisateurRepo.findByEmail("professeur.flemardo@irit.fr").orElse(null);
        assertNotNull(gestionnaire);
        assertEquals(RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU, gestionnaire.getRole());
        assertEquals("LaFl3ww", gestionnaire.getPseudo());

        // Vérifie la récompense spéciale
        Recompense badge = recompenseRepo.findByTitre("Procrastinateur en Danger").orElse(null);
        assertNotNull(badge);
        assertEquals("Échouer à un piège de productivité.", badge.getConditionsObtention());

        // Vérifie l'excuse spéciale
        ExcuseCreative excuse = excuseRepo.findByTexte("Excuse ultime de je n'avais pas le temps").orElse(null);
        assertNotNull(excuse);
        assertEquals("Excuse ultime de je n'avais pas le temps", excuse.getTexte());
    }
}
