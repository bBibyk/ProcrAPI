package org.bem.procrapi.services;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryParticipationDefi;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
class ServiceParticipationDefiTest {

    private RepositoryParticipationDefi participationRepo;
    private RepositoryDefiDeProcrastination defiRepo;
    private ServiceUtilisateur utilisateurService;
    private ServiceParticipationDefi participationService;

    @BeforeEach
    void setUp() {
        participationRepo = mock(RepositoryParticipationDefi.class);
        defiRepo = mock(RepositoryDefiDeProcrastination.class);
        utilisateurService = mock(ServiceUtilisateur.class);
        participationService = new ServiceParticipationDefi(participationRepo, defiRepo, utilisateurService);
    }

    @Test
    void testCreateRoleInvalide() {

        /* Créer un utilisateur anti-procrastinateur qui n'as pas le dorit de créer
        * une participation au défi
         */

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setRole(RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI);

        when(utilisateurService.getUtilisateurCourant()).thenReturn(utilisateur);

        // Exception est levée
        Exception exception = assertThrows(ServiceValidationException.class, () ->
                participationService.create("Test Participation Défi"));

        assertEquals("Seuls les Procrastinateurs en Herbe peuvent participer à un défi.", exception.getMessage());
    }

    @Test
    void testCreateReussi() {

        // Créer un utilisateur qui a le droit de participer à un défi
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setParticipations(new ArrayList<>());

        DefiDeProcrastination defi = new DefiDeProcrastination();
        defi.setParticipations(new ArrayList<>());

        ParticipationDefi participation = new ParticipationDefi();
        participation.setStatut(StatutParticipation.INSCRIT);
        participation.setPoints(50);

        // Simuler avec le mock
        when(utilisateurService.getUtilisateurCourant()).thenReturn(utilisateur);
        when(defiRepo.findByTitre("Test")).thenReturn(Optional.of(defi));
        when(participationRepo.save(any())).thenReturn(participation);

        // La méthode à tester c'est create via le service de participation défi
        ParticipationDefi resultat = participationService.create("Test");

        assertNotNull(resultat);
        // Vérifier les attributs concernés par l'instance participation
        assertEquals(StatutParticipation.INSCRIT, resultat.getStatut());
        assertEquals(50, resultat.getPoints());
    }
}