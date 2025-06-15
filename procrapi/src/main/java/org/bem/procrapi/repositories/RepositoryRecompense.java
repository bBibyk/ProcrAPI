package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.Recompense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * DAO pour les entités représentant les récompenses.
 */

public interface RepositoryRecompense extends JpaRepository<Recompense, Long> {
    Optional<Recompense> findByTitre(String titre);
}
