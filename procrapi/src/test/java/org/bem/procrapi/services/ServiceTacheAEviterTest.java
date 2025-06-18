package org.bem.procrapi.services;

import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class ServiceTacheAEviterTest {

    // Dépendances mockées
    RepositoryTacheAEviter tacheRepository;
    RepositoryUtilisateur utilisateurRepository;
    ServiceUtilisateur serviceUtilisateur;

    // Service à tester
    ServiceTacheAEviter serviceTache;

    // Données de test
    private Utilisateur utilisateur;
    private TacheAEviter tache;

    @BeforeEach
    void setUp() {
        tacheRepository = mock(RepositoryTacheAEviter.class);
        utilisateurRepository = mock(RepositoryUtilisateur.class);
        serviceUtilisateur = mock(ServiceUtilisateur.class);

        serviceTache = new ServiceTacheAEviter(tacheRepository, utilisateurRepository, serviceUtilisateur);

        // La partie GIVEN (Les données nécessaires pour les tests)
        utilisateur = new Utilisateur();
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);

        tache = new TacheAEviter();
        tache.setTitre("Test Tâche");
        tache.setDateLimite(LocalDate.now().plusDays(5));
        tache.setDegreUrgence(3);
        tache.setUtilisateur(utilisateur);
    }

    @Test
    void testCreateSucces() {
        // Simuler le comportement voulu par le service + les repos
        when(serviceUtilisateur.getUtilisateurCourant()).thenReturn(utilisateur);
        when(tacheRepository.findByTitre("Test Tâche")).thenReturn(Optional.empty());
        when(tacheRepository.save(any())).thenReturn(tache);

        // Action à tester
        TacheAEviter resultat = serviceTache.create(LocalDate.now().plusDays(5), "Test Tâche", 3, null, null);

        // THEN
        assertNotNull(resultat);
        assertEquals("Test Tâche", resultat.getTitre());
        assertEquals(3, resultat.getDegreUrgence());
        verify(tacheRepository).save(any());
    }

    @Test
    void testSetStatut() {

        utilisateur.setTaches(List.of(tache));

        // Simuler le comportement voulu
        when(serviceUtilisateur.getUtilisateurCourant()).thenReturn(utilisateur);
        when(utilisateurRepository.findById(utilisateur.getId())).thenReturn(Optional.of(utilisateur));
        when(tacheRepository.findByUtilisateurIdAndDateCompletionBetween(eq(utilisateur.getId()), any(), any()))
                .thenReturn(List.of(new TacheAEviter(), new TacheAEviter()));
        when(tacheRepository.save(any())).thenReturn(tache);

        // Action à tester
        TacheAEviter resultat = serviceTache.setStatut("Test Tâche", StatutTache.EVITE_AVEC_SUCCES);

        // THEN
        assertNotNull(resultat);
        verify(serviceUtilisateur, never()).perdreNiveau(any());
        verify(utilisateurRepository, never()).save(any());

    }

    @Test
    void testComputePointsRapportes_ValeurMax() {

        // GIVEN
        TacheAEviter tache = new TacheAEviter();
        tache.setDegreUrgence(5);
        tache.setDateLimite(LocalDate.now().minusDays(50));

        // WHEN
        int points = serviceTache.computePointsRapportes(tache, LocalDate.now());

        // THEN
        assertEquals(200, points);
        assertEquals(5, tache.getDegreUrgence());
    }

    @Test
    void testComputePointsRapportes_Correct() {
        TacheAEviter tache = new TacheAEviter();
        tache.setDegreUrgence(3);
        tache.setDateLimite(LocalDate.now().minusDays(2));

        int points = serviceTache.computePointsRapportes(tache, LocalDate.now());

        assertEquals(40, points); // 3*10 + 5*2
    }
}