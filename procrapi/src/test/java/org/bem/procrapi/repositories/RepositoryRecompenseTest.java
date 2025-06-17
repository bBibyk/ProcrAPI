package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryRecompenseTest {

    @Autowired
    private RepositoryRecompense repository;

    @BeforeEach
    void setUp() {
        assertNotNull(repository);

        Recompense recompense = new Recompense();
        recompense.setTitre("Recompense Test");
        recompense.setDescription("Description Test");
        recompense.setConditionsObtention("Conditions Obtention");
        recompense.setNiveau(NiveauDePrestige.FER);
        recompense.setType(TypeRecompense.BADGE);

        repository.save(recompense);

        // S’assurer que l’id du recompense est bien généré
        assertNotNull(recompense.getId());
    }

    @Test
    void testFindByTitre() {
        // Tester le cas où une recompense existe avec le titre fourni
        Optional<Recompense> trouvee = repository.findByTitre("Recompense Test");
        assertTrue(trouvee.isPresent(), "La récompense n'est pas trouvée");
        assertEquals(NiveauDePrestige.FER, trouvee.get().getNiveau());

        // Tester le cas où une recompense n'existe pas avec le titre fourni
        Optional<Recompense> absente = repository.findByTitre("Titre Inexistant");
        assertTrue(absente.isEmpty(), "Aucune récompense ne doit être trouvée avec ce titre");
    }

}
