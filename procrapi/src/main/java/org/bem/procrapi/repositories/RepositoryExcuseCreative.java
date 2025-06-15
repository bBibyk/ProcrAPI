package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * DAO pour les entités représentant les excuses créatives.
 */

public interface RepositoryExcuseCreative extends JpaRepository<ExcuseCreative, Long> {
    Optional<ExcuseCreative> findByTexte(String texte);

    /**
     * Recherche les excuses créatives par leur statut.
     *
     * @param statut le statut de l'excuse (EN_ATTENTE, APPROUVEE, REJETEE)
     * @return la liste des excuses correspondant à ce statut
     */
    List<ExcuseCreative> findByStatut(StatutExcuse statut);

    List<ExcuseCreative> findByStatutByVotesRecusDesc(StatutExcuse statut);
}
