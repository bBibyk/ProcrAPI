package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.AttributionRecompense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryAttributionRecompenseTest {

    @Autowired
    private RepositoryAttributionRecompense repository;

    @BeforeEach
    void setUp() {
        assertNotNull(repository);

        // Créer deux instances AttributionRecompense avec la même date d'expiration
        AttributionRecompense ar1 = new AttributionRecompense();
        ar1.setDateExpiration(LocalDate.of(2025, 8, 1));
        repository.save(ar1);

        AttributionRecompense ar2 = new AttributionRecompense();
        ar2.setDateExpiration(LocalDate.of(2025, 8, 1));
        repository.save(ar2);

        // S’assurer que chaque attribution recompense possède bien un ID généré
        assertNotNull(ar1.getId());
        assertNotNull(ar2.getId());
    }

    @Test
    void testFindByDateExpiration() {

        // Tester le cas où la date est présente 2 fois
        LocalDate dateTest = LocalDate.of(2025, 8, 1);
        List<AttributionRecompense> result = repository.findByDateExpiration(dateTest);

        assertNotNull(result);
        assertEquals(2, result.size());

        // Tester le cas où la date n'existe pas
        LocalDate dateInexistante = LocalDate.of(2025, 9, 1);
        List<AttributionRecompense> emptyResult = repository.findByDateExpiration(dateInexistante);

        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }
}
