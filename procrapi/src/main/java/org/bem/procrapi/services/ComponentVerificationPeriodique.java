package org.bem.procrapi.services;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ComponentVerificationPeriodique {

    @Scheduled(fixedRate = 60000)
    public void createUser(){
        System.out.println("works");
    }
}
