package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour l'entité Utilisateur.
 */
@Repository
public interface RepositoryUtilisateur extends JpaRepository<Utilisateur, Long> {

    /**
     * Recherche un utilisateur par son adresse email.
     * Utile pour l’authentification et la gestion des comptes.
     * @param email Adresse email unique de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé, sinon vide
     */
    Optional<Utilisateur> findByEmail(String email);
}
