package org.bem.procrapi.services;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
class ServiceRecompenseTest {

    private RepositoryRecompense repository;
    private ServiceRecompense service;

    @BeforeEach
    void setUp() {
        repository = mock(RepositoryRecompense.class);
        service = new ServiceRecompense(repository);
    }

    @Test
    void testCreateReussi(){

        // Créer la récompense qu'on veut obtenir
        Recompense recompense = new Recompense();
        recompense.setTitre("Test");
        recompense.setDescription("Desc");
        recompense.setConditionsObtention("Cond");
        recompense.setNiveau(NiveauDePrestige.BRONZE);
        recompense.setType(TypeRecompense.BADGE);

        // Simuler le comportement de RepositoryRecompense
        when(repository.findByTitre("Test")).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(recompense);

        // Créer un recompense via ServiceRecompense
        Recompense resultat = service.create("Test", "Desc", "Cond", NiveauDePrestige.BRONZE, TypeRecompense.BADGE);

        assertNotNull(resultat);

        // Vérifier tous les attributs
        assertEquals("Test", resultat.getTitre());
        assertEquals("Desc", resultat.getDescription());
        assertEquals(NiveauDePrestige.BRONZE, resultat.getNiveau());
        assertEquals(TypeRecompense.BADGE, resultat.getType());
        assertEquals("Cond",resultat.getConditionsObtention());
    }

    @Test
    void testCreateEchec() throws ServiceValidationException {
        // Tester le cas où le niveau n'est pas fourni par l'utilisateur
        when(repository.findByTitre("Test")).thenReturn(Optional.empty());

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () ->
                service.create("Test", "Description", "Conditions", null, TypeRecompense.BADGE)
        );
        assertEquals("Niveau de prestige non valide.", exception.getMessage());
    }
}