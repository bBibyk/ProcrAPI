package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ParticipationDefi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryParticipationDefi extends JpaRepository<ParticipationDefi, Long> {

}
