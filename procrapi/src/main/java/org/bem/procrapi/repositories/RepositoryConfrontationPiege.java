package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ConfrontationPiege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryConfrontationPiege extends JpaRepository<ConfrontationPiege, Long> {
}
