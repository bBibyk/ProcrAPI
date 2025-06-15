package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryDefiDeProcrastination extends JpaRepository<DefiDeProcrastination, Long> {
    Optional<DefiDeProcrastination> findByTitre(String id);
    List<DefiDeProcrastination> findByDateDebut(LocalDate dateDebut);
    List<DefiDeProcrastination> findByDateFin(LocalDate dateFin);
}
