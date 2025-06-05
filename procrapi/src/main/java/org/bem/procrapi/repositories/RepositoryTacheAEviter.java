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
    public List<TacheAEviter> findByUtilisateur(Utilisateur utilisateur);
    List<TacheAEviter> findByUtilisateurIdAndStatut(Long userId, StatutTache statut);

}
