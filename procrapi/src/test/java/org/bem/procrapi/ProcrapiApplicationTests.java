package org.bem.procrapi;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProcrapiApplicationTests {

    @Autowired
    private RepositoryUtilisateur repositoryUtilisateur;

    @Autowired
    private RepositoryRecompense repositoryRecompense;

    @Autowired
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @Test
    // On teste si les entités de base sont bien initialisées dans la BD
    void testDatabaseInitialization() {
        var userOpt = repositoryUtilisateur.findByEmail("professeur.flemardo@irit.fr");
        assertThat(userOpt).isPresent();
        Utilisateur user = userOpt.get();
        assertThat(user.getPseudo()).isEqualTo("LaFl3ww");
        assertThat(user.getRole()).isNotNull();

        var recompenseOpt = repositoryRecompense.findByTitre("Procrastinateur en Danger");
        assertThat(recompenseOpt).isPresent();
        Recompense recompense = recompenseOpt.get();
        assertThat(recompense.getConditionsObtention()).isEqualTo("Échouer à un piège de productivité.");

        var excuseOpt = repositoryExcuseCreative.findByTexte("Excuse ultime de je n'avais pas le temps");
        assertThat(excuseOpt).isPresent();
        ExcuseCreative excuse = excuseOpt.get();
        assertThat(excuse.getStatut()).isNotNull();
    }
}
