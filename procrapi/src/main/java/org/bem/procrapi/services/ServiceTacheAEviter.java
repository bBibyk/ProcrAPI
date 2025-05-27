package org.bem.procrapi.services;

import jakarta.transaction.Transactional;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceTacheAEviter {
    private RepositoryTacheAEviter repositoryTacheAEviter;
    private RepositoryUtilisateur repositoryUtilisateur;

    @Autowired
    public ServiceTacheAEviter(RepositoryTacheAEviter repositoryTacheAEviter,
                               RepositoryUtilisateur repositoryUtilisateur) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryTacheAEviter = repositoryTacheAEviter;
    }

    public Optional<TacheAEviter> create(TacheAEviter tacheAEviter){
        if(tacheAEviter.getDateLimite()==null){
            return Optional.empty();
        }
        Utilisateur currentUtilisateur = repositoryUtilisateur.findById(
                UtilisateurHolder.getCurrentUser().getId()).get();
        tacheAEviter.setUtilisateur(currentUtilisateur);
        return Optional.of(repositoryTacheAEviter.save(tacheAEviter));
    }
}
