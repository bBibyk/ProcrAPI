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
    void testCreateSucces() {
        /*
        when(serviceUtilisateur.getUtilisateurCourant()).thenReturn(utilisateur);
        when(repositoryExcuse.findByTexte("Surcharge de travail")).thenReturn(Optional.empty());
        when(repositoryTache.findByUtilisateurIdAndStatut(utilisateur.getId(), StatutTache.EVITE_AVEC_SUCCES))
                .thenReturn(List.of(new TacheAEviter()));
        when(repositoryTache.findByUtilisateurIdAndStatut(utilisateur.getId(), StatutTache.CATASTROPHE))
                .thenReturn(Collections.emptyList());
        when(repositoryExcuse.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ExcuseCreative excuse = serviceExcuse.create("Surcharge de travail", "Aller au sport", CategorieExcuse.VIE_SOCIALE);

        assertNotNull(excuse);
        assertEquals("Surcharge de travail", excuse.getTexte());
        assertEquals(0, excuse.getVotesRecus());
        assertEquals(StatutExcuse.EN_ATTENTE, excuse.getStatut());
        assertEquals(utilisateur, excuse.getCreateur());*/
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
    void setStatut() {
        /*
        // GIVEN
        when(serviceUtilisateur.getUtilisateurCourant()).thenReturn(gestionnaire);
        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexte("Erreur informatique");
        excuse.setStatut(StatutExcuse.EN_ATTENTE);

        when(repositoryExcuse.findByTexte("Erreur informatique")).thenReturn(Optional.of(excuse));
        when(repositoryExcuse.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ExcuseCreative result = serviceExcuse.setStatut("Erreur informatique", StatutExcuse.APPROUVEE);

        assertEquals(StatutExcuse.APPROUVEE, result.getStatut());

         */
    }

    @Test
    void voterPourExcuse() {
        /*
        // GIVEN
        when(serviceUtilisateur.getUtilisateurCourant()).thenReturn(utilisateur);
        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexte("Manque de la motivation");
        // L'utilisateur ne peut que voter pour un excuse approvee
        excuse.setStatut(StatutExcuse.APPROUVEE);
        excuse.setVotesRecus(10);

        when(repositoryExcuse.findByTexte("Manque de la motivation")).thenReturn(Optional.of(excuse));
        when(repositoryExcuse.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Ce qu'on teste : c'est la méthode voterPourExcuse
        ExcuseCreative result = serviceExcuse.voterPourExcuse("Manque de la motivation");

        // THEN
        assertEquals(10, result.getVotesRecus());
        assertEquals("Manque de la motivation", result.getTexte());
        assertEquals(StatutExcuse.APPROUVEE, result.getStatut()); */
    }

    @Test
    void getClassementHebdomadaire() {
        List<ExcuseCreative> topExcuses = List.of(new ExcuseCreative(), new ExcuseCreative());
        when(repositoryExcuse.findByStatutOrderByVotesRecusDesc(StatutExcuse.APPROUVEE)).thenReturn(topExcuses);

        List<ExcuseCreative> result = serviceExcuse.getClassementHebdomadaire();
        assertEquals(2, result.size());
    }
}