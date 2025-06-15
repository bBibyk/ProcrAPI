package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepositoryDefiDeProcrastination extends JpaRepository<DefiDeProcrastination, Long> {
    public List<DefiDeProcrastination> findByDateDebut(LocalDate dateDebut);
    public List<DefiDeProcrastination> findByDateFin(LocalDate dateFin);
}
