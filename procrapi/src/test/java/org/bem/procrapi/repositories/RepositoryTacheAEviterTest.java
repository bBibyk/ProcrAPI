package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryTacheAEviterTest {

    @Autowired
    private RepositoryTacheAEviter repositoryTaches;
    @Autowired
    private RepositoryUtilisateur repositoryUtilisateur;

    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        assertNotNull(repositoryTaches);
        assertNotNull(repositoryUtilisateur);

        //repositoryTaches.deleteAll();

        utilisateur = new Utilisateur();
        utilisateur.setPseudo("Utilisateur Test");
        utilisateur.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        utilisateur.setNiveau(NiveauProcrastination.DEBUTANT);
        utilisateur.setPointsAccumules(0);
        utilisateur.setDateInscription(LocalDate.of(2025, 8, 1));
        repositoryUtilisateur.save(utilisateur);

        TacheAEviter tache1 = new TacheAEviter();
        tache1.setTitre("Regarder le documentaire pour le CR d'anglais");
        tache1.setStatut(StatutTache.EN_ATTENTE);
        tache1.setUtilisateur(utilisateur);
        tache1.setDateCompletion(LocalDate.of(2025, 9, 10));
        repositoryTaches.save(tache1);

        TacheAEviter tache2 = new TacheAEviter();
        tache2.setTitre("Finir les DM");
        tache2.setStatut(StatutTache.EVITE_AVEC_SUCCES);
        tache2.setUtilisateur(utilisateur);
        tache2.setDateCompletion(LocalDate.of(2025, 8, 25));
        repositoryTaches.save(tache2);

        // S’assurer que les IDs des tâches et de l'utilisateur sont bien générés
        assertNotNull(tache1.getId());
        assertNotNull(tache2.getId());
        assertNotNull(utilisateur.getId());
    }

    @Test
    void testFindByTitre() {

        // Tester une tâche existante avec le titre fourni
        Optional<TacheAEviter> resultat = repositoryTaches.findByTitre("Finir les DM");
        assertTrue(resultat.isPresent());
        assertEquals(StatutTache.EVITE_AVEC_SUCCES, resultat.get().getStatut());
        assertEquals(LocalDate.of(2025, 8, 25), resultat.get().getDateCompletion());

        // Tester une tâche qui n'existe pas avec le titre fourni
        Optional<TacheAEviter> inExistante = repositoryTaches.findByTitre("Tâche inexistante");
        assertTrue(inExistante.isEmpty());
    }

    @Test
    void testFindByUtilisateurIdAndStatut() {

        // Tester le cas où l'utilisateur existe ET possède une tâche avec le statut "EN_ATTENTE"
        List<TacheAEviter> trouvees = repositoryTaches.findByUtilisateurIdAndStatut(utilisateur.getId(), StatutTache.EN_ATTENTE);
        assertEquals(1, trouvees.size());
        assertEquals("Regarder le documentaire pour le CR d'anglais", trouvees.get(0).getTitre());

        // Tester le cas où l'utilisateur existe MAIS testé avec une tâche ayant un statut non correspondant
        List<TacheAEviter> aucuneTacheAvecCeStatut = repositoryTaches.findByUtilisateurIdAndStatut(utilisateur.getId(), StatutTache.CATASTROPHE);
        assertTrue(aucuneTacheAvecCeStatut.isEmpty());

        // Tester le cas où l’utilisateur n’existe pas en base MAIS la tâche avec le statut "EN_ATTENTE" existe
        List<TacheAEviter> utilisateurInexistant = repositoryTaches.findByUtilisateurIdAndStatut(50L, StatutTache.EN_ATTENTE);
        assertTrue(utilisateurInexistant.isEmpty());

        // Tester le cas où ni l’utilisateur ni des tâches avec ce statut n’existent
        List<TacheAEviter> aucunResultat = repositoryTaches.findByUtilisateurIdAndStatut(50L, StatutTache.CATASTROPHE);
        assertTrue(aucunResultat.isEmpty(), "Aucun résultat attendu si ni l'utilisateur ni la tâche ne correspondent");
    }

    @Test
    void testFindByStatutOrStatut() {
        // Les statuts "expected"
        StatutTache statut1 = StatutTache.EN_ATTENTE;
        StatutTache statut2 = StatutTache.EVITE_AVEC_SUCCES;

        // Tester le cas général avec les tâches ayant soit statut1 soit statut2
        List<TacheAEviter> resultats = repositoryTaches.findByStatutOrStatut(statut1, statut2);
        assertEquals(2, resultats.size(), "2 tâches avec les statuts EN_ATTENTE ou EVITE_AVEC_SUCCES devraient etre trouvees");

        // Tester le cas où aucun statut ne correspond
        List<TacheAEviter> aucuneTache = repositoryTaches.findByStatutOrStatut(StatutTache.CATASTROPHE, StatutTache.REALISEE_IN_EXTREMIS);
        assertTrue(aucuneTache.isEmpty(), "Aucune tâche ne doit être trouvée avec des statuts inexistants en BD");
    }

    @Test
    void testFindByUtilisateurIdAndDateCompletionBetween() {
        // Créer une plage de dates qui inclut la dateCompletion des tâches
        LocalDate debut = LocalDate.of(2025, 8, 1);
        LocalDate fin = LocalDate.of(2025, 9, 30);

        // Tester le cas où les tâches de l'utilisateur avec dateCompletion est dans cette plage
        List<TacheAEviter> resultats = repositoryTaches.findByUtilisateurIdAndDateCompletionBetween(utilisateur.getId(), debut, fin);

        // Tester le cas où les 2 tâches sont dans cette plage, on s'attend donc à les retrouver
        assertEquals(2, resultats.size(), "Devrait trouver 2 tâches entre les dates spécifiées");

        // Tester le cas où la plage ne couvre aucune date de completion existante
        List<TacheAEviter> aucuneTache = repositoryTaches.findByUtilisateurIdAndDateCompletionBetween(utilisateur.getId(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        assertTrue(aucuneTache.isEmpty(), "Aucune tâche ne doit être trouvée hors de la plage de dates");

        // Tester le cas avec l'utilisateur inexistant, doit renvoyer liste vide
        List<TacheAEviter> utilisateurInexistant = repositoryTaches.findByUtilisateurIdAndDateCompletionBetween(99999L, debut, fin);
        assertTrue(utilisateurInexistant.isEmpty(), "Aucune tâche ne doit être trouvée pour un utilisateur inexistant");
    }
}