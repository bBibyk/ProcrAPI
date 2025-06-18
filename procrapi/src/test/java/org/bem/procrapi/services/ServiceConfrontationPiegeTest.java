package org.bem.procrapi.services;

import org.bem.procrapi.entities.ConfrontationPiege;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryConfrontationPiege;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
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
class ServiceConfrontationPiegeTest {

    RepositoryConfrontationPiege repoConfrontation;
    RepositoryPiegeDeProductivite repoPiege;
    ServiceUtilisateur svcUtilisateur;
    ServiceAttributionRecompense svcRecomp;
    ServiceConfrontationPiege svcConfrontation;

    @BeforeEach
    void setUp() {
        repoConfrontation = mock(RepositoryConfrontationPiege.class);
        repoPiege = mock(RepositoryPiegeDeProductivite.class);
        svcUtilisateur = mock(ServiceUtilisateur.class);
        svcRecomp = mock(ServiceAttributionRecompense.class);
        svcConfrontation = new ServiceConfrontationPiege(repoConfrontation, repoPiege, svcUtilisateur, svcRecomp);
    }

    @Test
    void testCreateReussi() {

        // Créer un utilisateur procrastinateur en herbe
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        PiegeDeProductivite piege = new PiegeDeProductivite();
        piege.setRecompense(new Recompense());
        ConfrontationPiege resultat = new ConfrontationPiege();
        resultat.setPoints(50);

        /* Simuler le comportement des repos + services
        injectés en dépendance
         */
        // Peu importe quel utilisateur est réellement connecté, je suppose que c'est l'instance utilisateur
        when(svcUtilisateur.getUtilisateurCourant()).thenReturn(utilisateur);
        when(repoPiege.findByTitre("test")).thenReturn(Optional.of(piege));
        when(repoConfrontation.save(any())).thenReturn(resultat);

        assertEquals(50, svcConfrontation.create("test", ResultatConfrontationPiege.SUCCES).getPoints());
    }

    @Test
    void testCreateEchec() throws ServiceValidationException {

        /* Tester le cas où l'utilisateur anti-procrastinateur
        * veut créer une confrontation piège alors qu'il n'as pas
        * le droit
         */
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setRole(RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI);
        when(svcUtilisateur.getUtilisateurCourant()).thenReturn(utilisateur);

        assertThrows(ServiceValidationException.class,
                () -> svcConfrontation.create("test", ResultatConfrontationPiege.SUCCES));
    }
}