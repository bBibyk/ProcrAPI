package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.StatutPiege;
import org.bem.procrapi.utilities.enumerations.TypePiege;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryPiegeDeProductiviteTest {

    @Autowired
    private RepositoryPiegeDeProductivite repository;

    @BeforeEach
    void setUp() {
        assertNotNull(repository);

        // Créer une instance de PiegeDeProductivite
        PiegeDeProductivite piege = new PiegeDeProductivite();
        piege.setTitre("Piège Test");
        piege.setDescription("Description du piège");
        piege.setType(TypePiege.DEFI);
        piege.setDifficulte(2);
        piege.setConsequence("Perte de temps");
        piege.setStatut(StatutPiege.ACTIF);
        repository.save(piege);

        // S’assurer que l’id du piège est bien généré
        assertNotNull(piege.getId());
    }

    @Test
    void testFindByTitre() {
        // Tester la méthode avec un piège avec le titre fourni qui existe
        Optional<PiegeDeProductivite> trouve = repository.findByTitre("Piège Test");
        assertTrue(trouve.isPresent());
        assertEquals("Piège Test", trouve.get().getTitre());

        // Tester avec un piège inexistant pour le titre fourni
        Optional<PiegeDeProductivite> absent = repository.findByTitre("Titre Inexistant");
        assertFalse(absent.isPresent(), "Aucun piège avec ce titre ne doit être trouvé");
    }
}