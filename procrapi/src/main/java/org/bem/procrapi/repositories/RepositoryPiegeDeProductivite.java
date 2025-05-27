package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.PiegeDeProductivite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryPiegeDeProductivite extends JpaRepository<PiegeDeProductivite, Long>{

    Optional<PiegeDeProductivite> findByTitre(String titre);
}
