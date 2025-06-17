package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.AttributionRecompense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class RepositoryAttributionRecompenseTest {

    @Autowired
    RepositoryAttributionRecompense repositoryAttribution;

    @Test
    void testfindByDateExpiration() {

        // Vérifier que le repository a bien été injecté
        assertNotNull(repositoryAttribution);

        // Crée une attribution de recompense avec une date d'expiration
        LocalDate dateExpiration = LocalDate.of(2025, 8, 1);
        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setDateExpiration(dateExpiration);

        // Sauvegarde en base
        attribution = repositoryAttribution.save(attribution);

        // Vérifie que l'ID a bien été généré
        assertNotNull(attribution.getId());

        // Tester avec une date qui existe dans la BD
        List<AttributionRecompense> result = repositoryAttribution.findByDateExpiration(dateExpiration);
        assertNotNull(result);
        assertTrue(result.contains(attribution));

        // Tester avec une date qui n'existe pas dans la BD
        LocalDate dateInexistante = LocalDate.of(2026, 12, 25);
        result = repositoryAttribution.findByDateExpiration(dateInexistante);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}