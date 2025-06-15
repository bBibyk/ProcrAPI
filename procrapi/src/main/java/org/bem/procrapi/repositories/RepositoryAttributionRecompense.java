package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.AttributionRecompense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
/**
 * DAO pour les entités représentant les attributions de récompenses.
 */

public interface RepositoryAttributionRecompense extends JpaRepository<AttributionRecompense, Long> {
    /**
     * Recherche les attributions de récompenses par date d'expiration.
     *
     * @param dateExpiration la date d'expiration recherchée
     * @return la liste des attributions avec cette date d'expiration
     */
    List<AttributionRecompense> findByDateExpiration(LocalDate dateExpiration);
}
