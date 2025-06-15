package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.AttributionRecompense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RepositoryAttributionRecompense extends JpaRepository<AttributionRecompense, Long> {
    List<AttributionRecompense> findByDateExpiration(LocalDate dateExpiration);
}
