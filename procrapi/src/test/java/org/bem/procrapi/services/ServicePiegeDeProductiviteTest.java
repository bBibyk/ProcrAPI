package org.bem.procrapi.services;

import org.bem.procrapi.authentication.EmailHolder;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.TypePiege;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
class ServicePiegeDeProductiviteTest {

    // Repository et Service qu'on va moquer
    RepositoryPiegeDeProductivite repositoryPiegeDeProductivite;
    RepositoryRecompense repositoryRecompense;
    ServiceUtilisateur serviceUtilisateur;

    // ServicePiegeDeProductivite qu'on va créer avec les repos mockés
    ServicePiegeDeProductivite servicePiegeDeProductivite;

    // Mock statique d'EmailHolder
    MockedStatic<EmailHolder> emailHolderMock;

    @BeforeEach
    void setUp() {
        // mock des repositories
        repositoryPiegeDeProductivite = mock(RepositoryPiegeDeProductivite.class);
        repositoryRecompense = mock(RepositoryRecompense.class);
        serviceUtilisateur = mock(ServiceUtilisateur.class);

        // création du service métier avec les mocks
        servicePiegeDeProductivite = new ServicePiegeDeProductivite(
                repositoryPiegeDeProductivite,
                serviceUtilisateur,
                repositoryRecompense
        );

        // mock statique d'EmailHolder
        emailHolderMock = Mockito.mockStatic(EmailHolder.class);
    }

    @AfterEach
    void tearDown() {
        emailHolderMock.close();
    }

    @Test
    void testCreate() {
        // Créer un utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setRole( RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI);

        Recompense recompense = new Recompense();
        recompense.setTitre("Recompense Test");

        PiegeDeProductivite piegeAttendu = new PiegeDeProductivite();
        piegeAttendu.setTitre("Piège Test");

        when(serviceUtilisateur.getUtilisateurCourant()).thenReturn(utilisateur);
        when(repositoryPiegeDeProductivite.findByTitre("Piège Test")).thenReturn(Optional.empty());
        when(repositoryRecompense.findByTitre("Recompense Test")).thenReturn(Optional.of(recompense));
        when(repositoryPiegeDeProductivite.save(any(PiegeDeProductivite.class))).thenReturn(piegeAttendu);

        // Résultat attendu
        PiegeDeProductivite resultat = servicePiegeDeProductivite.create("Piège Test", TypePiege.MEDITATION, 3, "Description test", "Recompense Test", "Conséquence test");

        // Assert
        assertNotNull(resultat);
        assertEquals("Piège Test", resultat.getTitre());
        verify(repositoryPiegeDeProductivite, times(1)).save(any(PiegeDeProductivite.class));
    }

    @Test
    void testCreateNonValide() throws ServiceValidationException {
        when(serviceUtilisateur.getUtilisateurCourant()).thenReturn(null);

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () ->
                servicePiegeDeProductivite.create("Titre", TypePiege.MEDITATION, 3, "Description", "Recompense", "Consequence")
        );

        assertEquals("Utilisateur non authentifié.", exception.getMessage());
    }

}