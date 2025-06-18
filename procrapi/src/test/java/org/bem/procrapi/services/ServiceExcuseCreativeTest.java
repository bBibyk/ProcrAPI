package org.bem.procrapi.services;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
class ServiceExcuseCreativeTest {

    @Autowired
    private RepositoryTacheAEviter repositoryTache;
    private RepositoryExcuseCreative repositoryExcuse;
    private RepositoryUtilisateur repositoryUtilisateur;
    private ServiceUtilisateur serviceUtilisateur;
    private ServiceExcuseCreative serviceExcuse;

    private Utilisateur utilisateur;
    private Utilisateur gestionnaire;

    @BeforeEach
    void setUp() {
        repositoryExcuse = mock(RepositoryExcuseCreative.class);
        repositoryUtilisateur = mock(RepositoryUtilisateur.class);
        serviceUtilisateur = mock(ServiceUtilisateur.class);

        serviceExcuse = new ServiceExcuseCreative(repositoryExcuse, repositoryTache, serviceUtilisateur);

        utilisateur = new Utilisateur();
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setTaches(new ArrayList<>());

        gestionnaire = new Utilisateur();
        gestionnaire.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
    }

    @Test
    void getExusesByStatutEchec() throws ServiceValidationException {

        // Tester le cas de création d'un excuse qui existe en "BD"
        when(repositoryExcuse.findByTexte("Déjà existante")).thenReturn(Optional.of(new ExcuseCreative()));

        Exception ex = assertThrows(ServiceValidationException.class, () ->
                serviceExcuse.create("Déjà existante", "Retard", CategorieExcuse.VIE_SOCIALE));

        assertEquals("Cette excuse existe déjà", ex.getMessage());
    }

    @Test
    void getClassementHebdomadaire() {
        List<ExcuseCreative> topExcuses = List.of(new ExcuseCreative(), new ExcuseCreative());
        when(repositoryExcuse.findByStatutOrderByVotesRecusDesc(StatutExcuse.APPROUVEE)).thenReturn(topExcuses);

        List<ExcuseCreative> result = serviceExcuse.getClassementHebdomadaire();
        assertEquals(2, result.size());
    }
}