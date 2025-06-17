package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.PiegeDeProductivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryPiegeDeProductivite extends JpaRepository<PiegeDeProductivite, Long>{
    Optional<PiegeDeProductivite> findByTitre(String titre);
}
