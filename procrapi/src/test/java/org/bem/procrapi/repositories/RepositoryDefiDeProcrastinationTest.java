package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryDefiDeProcrastinationTest {

    @Autowired
    private RepositoryDefiDeProcrastination repository;

    @BeforeEach
    void setUp() {
        assertNotNull(repository);

        DefiDeProcrastination defi1 = new DefiDeProcrastination();
        defi1.setTitre("Défi Test 1");
        defi1.setDateDebut(LocalDate.of(2026, 6, 1));
        defi1.setDateFin(LocalDate.of(2026, 6, 7));
        repository.save(defi1);

        DefiDeProcrastination defi2 = new DefiDeProcrastination();
        defi2.setTitre("Défi Test 2");
        defi2.setDateDebut(LocalDate.of(2027, 7, 1));
        defi2.setDateFin(LocalDate.of(2027, 7, 10));
        repository.save(defi2);

        DefiDeProcrastination defi3 = new DefiDeProcrastination();
        defi3.setTitre("Défi Test 3");
        defi3.setDateDebut(LocalDate.of(2025, 9, 1)); // même dateDebut que le premier
        defi3.setDateFin(LocalDate.of(2025, 9, 15));
        repository.save(defi3);

        // S’assurer que chaque défi possède bien un ID généré
        assertNotNull(defi1.getId());
        assertNotNull(defi2.getId());
        assertNotNull(defi3.getId());

    }

    @Test
    void findByTitre() {
        Optional<DefiDeProcrastination> result = repository.findByTitre("Défi Test 1");

        assertTrue(result.isPresent());
        assertEquals("Défi Test 1", result.get().getTitre(), "Le titre de défi ne correspond pas au défi trouvé");

        Optional<DefiDeProcrastination> absent = repository.findByTitre("Défi avec titre inexistant");
        assertTrue(absent.isEmpty());
    }

    @Test
    void findByDateDebut() {

        // Tester le cas où la date du début est présente
        LocalDate date = LocalDate.of(2026, 6, 1);
        List<DefiDeProcrastination> result = repository.findByDateDebut(date);

        assertNotNull(result);
        assertEquals(1, result.size(), "Le nombre de défis ne correspond pas à la date concernée");

        // Tester le cas où la date n'existe pas dans BD
        LocalDate dateInexistante = LocalDate.of(2030, 1, 1);
        List<DefiDeProcrastination> empty = repository.findByDateDebut(dateInexistante);

        assertNotNull(empty);
        assertTrue(empty.isEmpty());
    }

    @Test
    void findByDateFin() {

        // Tester le cas où la date de fin est présente
        LocalDate date = LocalDate.of(2025, 9, 15);
        List<DefiDeProcrastination> result = repository.findByDateFin(date);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2025, 9, 15), result.get(0).getDateFin());

        // Tester le cas où la date n'existe pas dans la BD
        List<DefiDeProcrastination> empty = repository.findByDateFin(LocalDate.of(2035, 1, 1));
        assertNotNull(empty);
        assertTrue(empty.isEmpty());
    }
}