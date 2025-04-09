package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryUtilisateur extends JpaRepository<Utilisateur, Long> {
    public Optional<Utilisateur> findByEmail(String email);
}
