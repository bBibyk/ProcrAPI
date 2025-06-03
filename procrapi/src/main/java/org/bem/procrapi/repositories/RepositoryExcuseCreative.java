package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.ExcuseCreative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryExcuseCreative extends JpaRepository<ExcuseCreative, Long> {
}
