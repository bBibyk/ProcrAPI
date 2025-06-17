package org.bem.procrapi.services;

import org.bem.procrapi.authentication.EmailHolder;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
class ServiceUtilisateurTest {

    @Autowired
    RepositoryUtilisateur repositoryUtilisateur;

    @Autowired
    RepositoryExcuseCreative repositoryExcuseCreative;

    ServiceUtilisateur serviceUtilisateur;

    MockedStatic<EmailHolder> emailHolderMock;

    @BeforeEach
    void setUp() {
        // Initialisation du service avec les repositories injectés
        serviceUtilisateur = new ServiceUtilisateur(repositoryUtilisateur, repositoryExcuseCreative);
        // On mock statiquement EmailHolder.getEmail() pour simuler l'utilisateur courant
        emailHolderMock = Mockito.mockStatic(EmailHolder.class);
    }

    @AfterEach
    void tearDown() {
        // Pour ne pas polluer les autres tests
        emailHolderMock.close();
    }

    @Test
    void testGetUtilisateurCourantAvecException() throws ServiceValidationException {

        // Tester le cas où l'utilisateur courant n'est pas trouvé en base, on attend une exception
        emailHolderMock.when(EmailHolder::getEmail).thenReturn("inexistant@example.com");
        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> {
            serviceUtilisateur.getUtilisateurCourant();
        });

        assertEquals("Vous n'êtes pas authentifié.", exception.getMessage());
    }

    @Test
    void testGetUtilisateurCourantReussi() throws ServiceValidationException {

        // Créer un utilisateur dans la BD
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("test@example.com");
        utilisateur.setPseudo("test");
        utilisateur.setRole(RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI);
        repositoryUtilisateur.save(utilisateur);

        // Tester le cas où l'utilisateur courant est trouvé et retourné correctement
        emailHolderMock.when(EmailHolder::getEmail).thenReturn("test@example.com");
        Utilisateur resultat = serviceUtilisateur.getUtilisateurCourant();
        assertEquals(utilisateur.getEmail(), resultat.getEmail());
    }

    @Test
    void testCreate_Echec() throws ServiceValidationException {
        /*
         * Tester le cas où on essaie de créer un ANTIPROCRASTINATEUR_REPENTI
         * sans être GESTIONNAIRE_DU_TEMPS_PERDU
         */
        String email = "utilisateur@example.com";
        String pseudo = "utilisateurTest";
        RoleUtilisateur role = RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI;

        /* Créer et un utilisateur courant qui
        * n'EST PAS gestionnaire,donc qui n'as pas droit
        * de créer des utilisateurs
         */

        Utilisateur utilisateurCourant = new Utilisateur();
        utilisateurCourant.setEmail("courant@example.com");
        utilisateurCourant.setPseudo("courant");
        utilisateurCourant.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        repositoryUtilisateur.save(utilisateurCourant);

        // Mock seulement EmailHolder
        emailHolderMock.when(EmailHolder::getEmail).thenReturn("courant@example.com");

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> {
            serviceUtilisateur.create(role, pseudo, email, null);
        });

        assertEquals("Seul le Gestionnaire du temps perdu peut créer des antiprocrastinateurs répantis.", exception.getMessage());
    }

    @Test
    void testCreate() {

        // Création des données utilisateur qui va être créer par le gestionnaire
        RoleUtilisateur role = RoleUtilisateur.PROCRASTINATEUR_EN_HERBE;
        String pseudo = "userTest";
        String email = "user@test.com";

        // Créer un utilisateur gestionnaire
        Utilisateur utilisateurCourant = new Utilisateur();
        utilisateurCourant.setEmail("admin@test.com");
        utilisateurCourant.setRole(RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU);
        repositoryUtilisateur.save(utilisateurCourant);

        // Mock de EmailHolder pour simuler l'utilisateur courant
        emailHolderMock.when(EmailHolder::getEmail).thenReturn("admin@test.com");

        try {
            Utilisateur utilisateurCree = serviceUtilisateur.create(role, pseudo, email, null);
            assertNotNull(utilisateurCree);
            assertEquals(email, utilisateurCree.getEmail());
            assertEquals(pseudo, utilisateurCree.getPseudo());
            assertEquals(role, utilisateurCree.getRole());

            // Vérifier que l'utilisateur a bien été sauvegardé
            Optional<Utilisateur> utilisateurTrouve = repositoryUtilisateur.findByEmail(email);
            assertTrue(utilisateurTrouve.isPresent());
            assertEquals(pseudo, utilisateurTrouve.get().getPseudo());
        } catch (ServiceValidationException e) {
            fail("Exception non attendue: " + e.getMessage());
        }
    }

    @Test
    void testAttribuerPoints() {

        // Créer un utilisateur pour tester
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPointsAccumules(300);
        utilisateur.setNiveau(NiveauProcrastination.DEBUTANT);
        repositoryUtilisateur.save(utilisateur);

        // Tester quand l'utilisateur DEBUTANT devient INTERMEDIAIRE
        serviceUtilisateur.attribuerPoints(utilisateur, 250);
        assertEquals(550, utilisateur.getPointsAccumules());
        assertEquals(NiveauProcrastination.INTERMEDIAIRE, utilisateur.getNiveau());
    }

    @Test
    void testPerdreNiveau() {
        // Tester quand l'utilisateur avec un niveau EXPERT devient INTERMEDIAIRE
        Utilisateur expert = new Utilisateur();
        expert.setNiveau(NiveauProcrastination.EXPERT);
        serviceUtilisateur.perdreNiveau(expert);
        assertEquals(NiveauProcrastination.INTERMEDIAIRE, expert.getNiveau());
    }
}