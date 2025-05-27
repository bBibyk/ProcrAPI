package org.bem.procrapi.services;

import org.bem.procrapi.repositories.RepositoryDefiDeProcrastination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDefiDeProcrastination {
    private RepositoryDefiDeProcrastination repositoryDefiDeProcrastination;

    @Autowired
    public ServiceDefiDeProcrastination(RepositoryDefiDeProcrastination repositoryDefiDeProcrastination) {
        this.repositoryDefiDeProcrastination = repositoryDefiDeProcrastination;
    }
}
