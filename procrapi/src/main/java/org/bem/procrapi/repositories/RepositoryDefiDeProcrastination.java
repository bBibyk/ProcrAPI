package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryDefiDeProcrastination extends JpaRepository<DefiDeProcrastination, Long> {

    /**
     * Recherche un défi par son titre exact.
     * @param titre Titre du défi recherché
     * @return Optional contenant le défi si trouvé, sinon vide
     */
    Optional<DefiDeProcrastination> findByTitre(String titre);

    /**
     * Recherche les défis dont la date de début correspond à la date fournie.
     * Utile pour récupérer tous les défis lancés un jour donné.
     * @param dateDebut Date de début du défi
     * @return Liste des défis démarrant à cette date
     */
    List<DefiDeProcrastination> findByDateDebut(LocalDate dateDebut);

    /**
     * Recherche les défis dont la date de fin correspond à la date fournie.
     * Permet d’identifier les défis terminant un jour précis.
     * @param dateFin Date de fin du défi
     * @return Liste des défis terminant à cette date
     */
    List<DefiDeProcrastination> findByDateFin(LocalDate dateFin);
}
