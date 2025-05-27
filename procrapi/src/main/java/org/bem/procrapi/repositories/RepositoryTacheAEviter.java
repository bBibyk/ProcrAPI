package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.TacheAEviter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTacheAEviter extends JpaRepository<TacheAEviter, Long> {
}
