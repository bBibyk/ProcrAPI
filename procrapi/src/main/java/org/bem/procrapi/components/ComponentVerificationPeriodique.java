package org.bem.procrapi.components;

import jakarta.transaction.Transactional;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceTacheAEviter;
import org.bem.procrapi.services.ServiceUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ComponentVerificationPeriodique {
    private final ServiceUtilisateur serviceUtilisateur;
    private final ServiceTacheAEviter serviceTacheAEviter;
    private final RepositoryUtilisateur repositoryUtilisateur;
    private final RepositoryTacheAEviter repositoryTacheAEviter;

    @Autowired
    public ComponentVerificationPeriodique(RepositoryUtilisateur repositoryUtilisateur,
                                           RepositoryTacheAEviter repositoryTacheAEviter, ServiceUtilisateur serviceUtilisateur, ServiceTacheAEviter serviceTacheAEviter) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryTacheAEviter = repositoryTacheAEviter;
        this.serviceUtilisateur = serviceUtilisateur;
        this.serviceTacheAEviter = serviceTacheAEviter;
    }

    @Scheduled(fixedRate = 60000)
    public void createUser(){
        System.out.println("works");
    }

    //Normalement on met 84400000 pour 1fois/24h
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void reevaluatePointsTaches(){
        List<TacheAEviter> taches = repositoryTacheAEviter.findByStatutOrStatut(StatutTache.EVITE_AVEC_SUCCES,
                StatutTache.CATASTROPHE);
        for(TacheAEviter tache : taches){
            Utilisateur utilisateur = repositoryUtilisateur.findById(tache.getUtilisateur().getId()).get();
            int pointsAvant = serviceTacheAEviter.computePointsRapportes(tache, LocalDate.now().minusDays(1));
            int pointsAujourdhui = serviceTacheAEviter.computePointsRapportes(tache, LocalDate.now());
            int pointsMerites = pointsAujourdhui - pointsAvant;
            serviceUtilisateur.attribuerPoints(utilisateur, pointsMerites);
        }
    }

    // TODO faire la méthode quotidienne pour finir un défis et setStatutParticipation

    // TODO une méthode pour terminer les récompenses
}
