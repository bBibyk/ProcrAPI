package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryTacheAEviter extends JpaRepository<TacheAEviter, Long> {
    List<TacheAEviter> findByUtilisateurIdAndStatut(Long userId, StatutTache statut);
    List<TacheAEviter> findByStatutOrStatut(StatutTache statut1, StatutTache statut2);

}
