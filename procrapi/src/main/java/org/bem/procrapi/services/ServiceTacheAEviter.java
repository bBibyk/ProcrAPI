package org.bem.procrapi.services;

import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTacheAEviter {
    private RepositoryTacheAEviter repositoryTacheAEviter;

    @Autowired
    public ServiceTacheAEviter(RepositoryTacheAEviter repositoryTacheAEviter) {
        this.repositoryTacheAEviter = repositoryTacheAEviter;
    }
}
