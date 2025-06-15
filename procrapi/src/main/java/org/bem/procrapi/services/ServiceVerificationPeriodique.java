package org.bem.procrapi.services;

import jakarta.transaction.Transactional;
import org.bem.procrapi.entities.*;
import org.bem.procrapi.repositories.*;
import org.bem.procrapi.utilities.enumerations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceVerificationPeriodique {
    private final ServiceUtilisateur serviceUtilisateur;
    private final ServiceTacheAEviter serviceTacheAEviter;
    private final RepositoryUtilisateur repositoryUtilisateur;
    private final RepositoryTacheAEviter repositoryTacheAEviter;
    private final RepositoryDefiDeProcrastination repositoryDefiDeProcrastination;
    private final RepositoryAttributionRecompense repositoryAttributionRecompense;
    private final RepositoryExcuseCreative repositoryExcuseCreative;

    @Autowired
    public ServiceVerificationPeriodique(RepositoryUtilisateur repositoryUtilisateur,
                                         RepositoryTacheAEviter repositoryTacheAEviter,
                                         ServiceUtilisateur serviceUtilisateur,
                                         ServiceTacheAEviter serviceTacheAEviter,
                                         RepositoryDefiDeProcrastination repositoryDefiDeProcrastination,
                                         RepositoryAttributionRecompense repositoryAttributionRecompense, RepositoryExcuseCreative repositoryExcuseCreative) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryTacheAEviter = repositoryTacheAEviter;
        this.serviceUtilisateur = serviceUtilisateur;
        this.serviceTacheAEviter = serviceTacheAEviter;
        this.repositoryDefiDeProcrastination = repositoryDefiDeProcrastination;
        this.repositoryAttributionRecompense = repositoryAttributionRecompense;
        this.repositoryExcuseCreative = repositoryExcuseCreative;
    }

    // 84400000 pour 1 fois/24 h
    @Scheduled(fixedRate = 84400000)
    @Transactional
    public void reevaluerPointsTachesEvitees(){
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

    // Pour éviter le problème du défis qui commence et termine à la même seconde on n'utilisera pas (fixedRate = 10000)
    // Mais plutot un scheduler par heure pour commencer à minuit
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void commencerDefis(){
        List<DefiDeProcrastination> defisCommencant = repositoryDefiDeProcrastination.findByDateDebut(LocalDate.now());
        for(DefiDeProcrastination defi : defisCommencant){
            defi.setStatut(StatutDefi.EN_COURS);
            for(ParticipationDefi participation : defi.getParticipations()){
                participation.setStatut(StatutParticipation.EN_COURS);
            }
        }
    }

    // Et de même pour terminer avant minuit
    @Scheduled(cron = "0 59 23 * * *")
    @Transactional
    public void terminerDefis(){
        List<DefiDeProcrastination> defisCommencant = repositoryDefiDeProcrastination.findByDateFin(LocalDate.now());
        for(DefiDeProcrastination defi : defisCommencant){
            defi.setStatut(StatutDefi.TERMINE);
            for(ParticipationDefi participation : defi.getParticipations()){
                participation.setStatut(StatutParticipation.TERMINE);
            }
        }
    }

    // Tous les jours après minuit (et une minute pour ne pas surcharger le serveur avec les deux traitements simultanés)
    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void terminerAttributionsRecompensesExpires(){
        List<AttributionRecompense> attributions = repositoryAttributionRecompense.findByDateExpiration(LocalDate
                .now()
                .minusDays(1)
        );
        for(AttributionRecompense attribution : attributions){
            attribution.setStatut(StatutRecompense.EXPIRE);
        }
    }

    // Tous les 7 jours (hebdo) les votes sont remis à 0
    @Scheduled(fixedRate = 604800000)
    @Transactional
    public void reinitialiserVotesExcusesValide(){
        List<ExcuseCreative> excuses = repositoryExcuseCreative.findByStatut(StatutExcuse.APPROUVEE);
        for(ExcuseCreative excuse : excuses){
            excuse.setVotesRecus(0);
        }
    }
}
