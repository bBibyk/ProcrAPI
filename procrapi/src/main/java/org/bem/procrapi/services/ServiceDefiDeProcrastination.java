package org.bem.procrapi.services;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceDefiDeProcrastination {
    private RepositoryDefiDeProcrastination repositoryDefiDeProcrastination;
    private RepositoryUtilisateur repositoryUtilisateur;

    @Autowired
    public ServiceDefiDeProcrastination(RepositoryDefiDeProcrastination repositoryDefiDeProcrastination,
                                        RepositoryUtilisateur repositoryUtilisateur) {
        this.repositoryDefiDeProcrastination = repositoryDefiDeProcrastination;
        this.repositoryUtilisateur = repositoryUtilisateur;
    }


    public Optional<DefiDeProcrastination> create(DefiDeProcrastination defi){
        if(defi.getDateFin()==null){
            return Optional.empty();
        }
        if(defi.getDuree()==null){
            return Optional.empty();
        }
        Utilisateur currentUtilisateur = repositoryUtilisateur.findById(
                UtilisateurHolder.getCurrentUser().getId()).get();
        defi.setCreateur(currentUtilisateur);
        return Optional.of(repositoryDefiDeProcrastination.save(defi));
    }
}