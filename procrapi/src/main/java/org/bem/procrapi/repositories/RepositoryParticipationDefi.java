package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ParticipationDefi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Repository JPA pour l'entité ParticipationDefi.
 */
public interface RepositoryParticipationDefi extends JpaRepository<ParticipationDefi, Long> {

}
