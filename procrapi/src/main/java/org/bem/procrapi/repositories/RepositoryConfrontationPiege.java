package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ConfrontationPiege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour l'entité ConfrontationPiege.
 * Fournit les opérations CRUD de base via JpaRepository.
 */
@Repository
public interface RepositoryConfrontationPiege extends JpaRepository<ConfrontationPiege, Long> {
}
