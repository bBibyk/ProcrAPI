package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryAttributionRecompense extends JpaRepository<AttributionRecompense, Long> {
    List<AttributionRecompense> findByRecompense_Type(TypeRecompense recompense_type);



}
