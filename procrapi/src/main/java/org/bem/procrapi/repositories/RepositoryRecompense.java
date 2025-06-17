package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.Recompense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO pour les entités représentant les récompenses.
 */
@Repository
public interface RepositoryRecompense extends JpaRepository<Recompense, Long> {
    Optional<Recompense> findByTitre(String titre);
}
