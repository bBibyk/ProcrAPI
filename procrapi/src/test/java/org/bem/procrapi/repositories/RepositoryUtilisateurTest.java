package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryUtilisateurTest {

    @Autowired
    private RepositoryUtilisateur repository;


    @BeforeEach
    void setUp() {
        assertNotNull(repository);

        Utilisateur utilisateur = new Utilisateur();
        // Créer une instance d'un utilisateur test
        utilisateur = new Utilisateur();
        utilisateur.setEmail("test@example.com");
        utilisateur.setPseudo("TestUser");
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setNiveau(NiveauProcrastination.DEBUTANT);
        utilisateur.setPointsAccumules(10);
        utilisateur.setDateInscription(LocalDate.of(2025, 6, 17));

        repository.save(utilisateur);

        assertNotNull(utilisateur.getId());
    }

    @Test
    void findByEmail() {

        // Tester avec un email existant
        Optional<Utilisateur> trouve = repository.findByEmail("test@example.com");
        assertTrue(trouve.isPresent(), "L'utilisateur n'est pas trouvé par son email");
        assertEquals("TestUser", trouve.get().getPseudo(), "Le pseudo doit correspondre");

        // Tester avec un email inexistant
        Optional<Utilisateur> nonTrouve = repository.findByEmail("inexistant@example.com");
        assertTrue(nonTrouve.isEmpty());

    }
}