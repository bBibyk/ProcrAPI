package org.bem.procrapi.services;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ServiceUtilisateur {
    private RepositoryUtilisateur repositoryUtilisateur;

    @Autowired
    public ServiceUtilisateur(RepositoryUtilisateur repositoryUtilisateur) {
        this.repositoryUtilisateur = repositoryUtilisateur;
    }

    public Optional<RoleUtilisateur> authentifyUser(String email) {
        Optional<Utilisateur> user = repositoryUtilisateur.findByEmail(email);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(user.get().getRole());
    }
}
