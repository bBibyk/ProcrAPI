package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryExcuseCreative extends JpaRepository<ExcuseCreative, Long> {
    List<ExcuseCreative> findByStatut(StatutExcuse statut);

}
