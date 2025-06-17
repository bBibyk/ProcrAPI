package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.utilities.enumerations.DifficulteDefi;
import org.bem.procrapi.utilities.enumerations.StatutDefi;
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

    @Test
    public void testFindByTitre() {
        assertNotNull(repository);

        DefiDeProcrastination defi = new DefiDeProcrastination();
        defi.setTitre("DefiUnique");
        defi.setDescription("Description");
        defi.setDuree(3);
        defi.setDifficulte(DifficulteDefi.MOYEN);
        defi.setPointsAGagner(50);
        defi.setDateDebut(LocalDate.of(2025, 1, 1));
        defi.setDateFin(LocalDate.of(2025, 1, 4));
        defi.setStatut(StatutDefi.INSCRIT);

        defi = repository.save(defi);
        assertNotNull(defi.getId());

        // Test findByTitre (Optional)
        Optional<DefiDeProcrastination> result = repository.findByTitre("DefiUnique");
        assertTrue(result.isPresent());
        assertEquals(defi.getId(), result.get().getId());

        // Test titre inexistant
        Optional<DefiDeProcrastination> emptyResult = repository.findByTitre("TitreInexistant");
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    public void testFindByDateDebut() {
        DefiDeProcrastination defi1 = new DefiDeProcrastination();
        defi1.setTitre("Defi1");
        defi1.setDateDebut(LocalDate.of(2025, 5, 10));
        defi1.setDateFin(LocalDate.of(2025, 5, 15));
        repository.save(defi1);

        DefiDeProcrastination defi2 = new DefiDeProcrastination();
        defi2.setTitre("Defi2");
        defi2.setDateDebut(LocalDate.of(2025, 5, 10));
        defi2.setDateFin(LocalDate.of(2025, 5, 20));
        repository.save(defi2);

        List<DefiDeProcrastination> result = repository.findByDateDebut(LocalDate.of(2025, 5, 10));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getTitre().equals("Defi1")));
        assertTrue(result.stream().anyMatch(d -> d.getTitre().equals("Defi2")));

        // Test dateDebut sans résultat
        List<DefiDeProcrastination> emptyResult = repository.findByDateDebut(LocalDate.of(2024, 1, 1));
        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    public void testFindByDateFin() {
        DefiDeProcrastination defi1 = new DefiDeProcrastination();
        defi1.setTitre("DefiFin1");
        defi1.setDateDebut(LocalDate.of(2025, 6, 1));
        defi1.setDateFin(LocalDate.of(2025, 6, 30));
        repository.save(defi1);

        DefiDeProcrastination defi2 = new DefiDeProcrastination();
        defi2.setTitre("DefiFin2");
        defi2.setDateDebut(LocalDate.of(2025, 6, 10));
        defi2.setDateFin(LocalDate.of(2025, 6, 30));
        repository.save(defi2);

        List<DefiDeProcrastination> result = repository.findByDateFin(LocalDate.of(2025, 6, 30));
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getTitre().equals("DefiFin1")));
        assertTrue(result.stream().anyMatch(d -> d.getTitre().equals("DefiFin2")));

        // Test dateFin sans résultat
        List<DefiDeProcrastination> emptyResult = repository.findByDateFin(LocalDate.of(2023, 12, 31));
        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }
}