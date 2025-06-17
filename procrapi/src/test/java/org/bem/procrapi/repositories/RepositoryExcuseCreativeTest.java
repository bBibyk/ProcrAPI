package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryExcuseCreativeTest {

    @Autowired
    private RepositoryExcuseCreative repository;

    @BeforeEach
    void setUp() {
        assertNotNull(repository);
        // Pour s'assurer que BD est bien vide au début
        repository.deleteAll();

        /*
         Instanciation des excuses avec :
          - différents titres,
         - catégories d'excuses,
         - statuts d'excuses
         */
        ExcuseCreative excuse1 = new ExcuseCreative();
        excuse1.setTexte("Mon ordi a explosé");
        excuse1.setSituation("Examen");
        excuse1.setCategorie(CategorieExcuse.ETUDES);
        excuse1.setStatut(StatutExcuse.APPROUVEE);
        excuse1.setVotesRecus(40);
        excuse1.setDateSoumission(LocalDate.of(2025, 8, 10));
        repository.save(excuse1);

        ExcuseCreative excuse2 = new ExcuseCreative();
        excuse2.setTexte("Je devais aller à un enterrement");
        excuse2.setSituation("Projet de groupe");
        excuse2.setCategorie(CategorieExcuse.VIE_SOCIALE);
        excuse2.setStatut(StatutExcuse.APPROUVEE);
        excuse2.setVotesRecus(20);
        excuse2.setDateSoumission(LocalDate.of(2025, 8, 12));
        repository.save(excuse2);

        ExcuseCreative excuse3 = new ExcuseCreative();
        excuse3.setTexte("J’ai eu une surcharge de boulot");
        excuse3.setSituation("Rapport final");
        excuse3.setCategorie(CategorieExcuse.TRAVAIL);
        excuse3.setStatut(StatutExcuse.EN_ATTENTE);
        excuse3.setVotesRecus(20);
        excuse3.setDateSoumission(LocalDate.of(2025, 6, 20));
        repository.save(excuse3);

        // S’assurer que chaque excuse possède bien un ID généré
        assertNotNull(excuse1.getId());
        assertNotNull(excuse2.getId());
        assertNotNull(excuse3.getId());
    }

    @Test
    void testFindByTexte() {

        // Tester le cas où l'excuse avec le titre fourni existe dans BD
        Optional<ExcuseCreative> trouvee = repository.findByTexte("Mon ordi a explosé");
        assertTrue(trouvee.isPresent());
        assertEquals("Examen", trouvee.get().getSituation());

        // Tester le cas où l'excuse avec le titre fourni n'existe pas
        Optional<ExcuseCreative> notFound = repository.findByTexte("Excuse absente");
        assertTrue(notFound.isEmpty());

    }

    @Test
    void testFindByStatut() {

        // Tester le nombre d'excuses dont le statut est "Approuvee"
        List<ExcuseCreative> approuvees = repository.findByStatut(StatutExcuse.APPROUVEE);
        assertEquals(2, approuvees.size());

        // Tester le nombre d'excuses dont le statut est "En_Attente"
        List<ExcuseCreative> enAttente = repository.findByStatut(StatutExcuse.EN_ATTENTE);
        assertEquals(1, enAttente.size());
        assertEquals("J’ai eu une surcharge de boulot", enAttente.get(0).getTexte());

        // Tester le nombre d'excuses dont le statut est "Rejetees", ce qui est 0 dans ce cas de test
        List<ExcuseCreative> rejetees = repository.findByStatut(StatutExcuse.REJETEE);
        assertTrue(rejetees.isEmpty());
    }

    @Test
    void testFindByStatutOrderByVotesRecusDesc() {

        // Tester le nombre de votes reçus dont le statut est "Approuvee"
        List<ExcuseCreative> resultApprouvees = repository.findByStatutOrderByVotesRecusDesc(StatutExcuse.APPROUVEE);
        assertEquals(2, resultApprouvees.size());
        assertEquals(40, (int) resultApprouvees.get(0).getVotesRecus());
        assertEquals(20, (int) resultApprouvees.get(1).getVotesRecus());

        // Tester le nombre de votes reçus dont le statut est "En_Attente"
        List<ExcuseCreative> resultEnAttente = repository.findByStatutOrderByVotesRecusDesc(StatutExcuse.EN_ATTENTE);
        assertEquals(1, resultEnAttente.size());
        assertEquals(20, resultEnAttente.get(0).getVotesRecus());
    }
}