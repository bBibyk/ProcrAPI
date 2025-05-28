package org.bem.procrapi.services;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceUtilisateur {

    private final RepositoryUtilisateur repositoryUtilisateur;
    private final RepositoryExcuseCreative repositoryExcuseCreative;

    @Autowired
    public ServiceUtilisateur(RepositoryUtilisateur repositoryUtilisateur, RepositoryExcuseCreative repositoryExcuseCreative) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryExcuseCreative = repositoryExcuseCreative;
    }

    public Optional<Utilisateur> create(Utilisateur utilisateur) {
        if (utilisateur.getRole() == null) {
            return Optional.empty();
        }
        if (repositoryUtilisateur.findByEmail(utilisateur.getEmail()).isPresent()) {
            return Optional.empty();
        }
        Utilisateur savedUser = repositoryUtilisateur.save(utilisateur);
        return Optional.of(savedUser);
    }
}
