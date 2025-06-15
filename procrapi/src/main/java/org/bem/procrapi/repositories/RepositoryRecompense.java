package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.Recompense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO pour les entités représentant les récompenses.
 */

public interface RepositoryRecompense extends JpaRepository<Recompense, Long> {
    boolean findRecompenseByTitre(String titre);

    Recompense getRecompenseByTitre(String titre);
}
