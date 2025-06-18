package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité TacheAEviter.
 */
@Repository
public interface RepositoryTacheAEviter extends JpaRepository<TacheAEviter, Long> {

    /**
     * Recherche une tâche à éviter par son titre exact.
     * @param titre Titre de la tâche
     * @return Optional contenant la tâche si trouvée, sinon vide
     */
    Optional<TacheAEviter> findByTitre(String titre);

    /**
     * Récupère les tâches d’un utilisateur selon leur statut.
     * Utile pour filtrer tâches en attente, complétées, etc.
     * @param userId Identifiant de l'utilisateur
     * @param statut Statut de la tâche (ex : EN_ATTENTE, COMPLETÉ)
     * @return Liste des tâches correspondant aux critères
     */
    List<TacheAEviter> findByUtilisateurIdAndStatut(Long userId, StatutTache statut);

    /**
     * Récupère les tâches ayant l’un des deux statuts fournis.
     * Pratique pour regrouper plusieurs états (ex: EN_ATTENTE ou EN_COURS).
     * @param statut1 Premier statut
     * @param statut2 Second statut
     * @return Liste des tâches correspondant à l’un des deux statuts
     */
    List<TacheAEviter> findByStatutOrStatut(StatutTache statut1, StatutTache statut2);

    /**
     * Récupère les tâches complétées par un utilisateur dans une période donnée.
     * Utile pour les statistiques ou règles de déchéance basées sur dates de complétion.
     * @param utilisateurId Identifiant de l’utilisateur
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des tâches complétées dans la plage donnée
     */
    List<TacheAEviter> findByUtilisateurIdAndDateCompletionBetween(Long utilisateurId, LocalDate debut, LocalDate fin);
}
