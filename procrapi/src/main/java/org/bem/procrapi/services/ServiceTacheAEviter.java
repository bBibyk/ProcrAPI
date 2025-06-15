package org.bem.procrapi.services;

import org.bem.procrapi.components.authentication.EmailHolder;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ServiceTacheAEviter {
    private RepositoryTacheAEviter repositoryTacheAEviter;
    private RepositoryUtilisateur repositoryUtilisateur;
    private ServiceUtilisateur serviceUtilisateur;

    @Autowired
    public ServiceTacheAEviter(RepositoryTacheAEviter repositoryTacheAEviter,
                               RepositoryUtilisateur repositoryUtilisateur,
                               ServiceUtilisateur serviceUtilisateur) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryTacheAEviter = repositoryTacheAEviter;
        this.serviceUtilisateur = serviceUtilisateur;
    }

    public TacheAEviter create(TacheAEviter tacheAEviter){
        Utilisateur utilisateurCourant = serviceUtilisateur.getUtilisateurCourant();
        if (utilisateurCourant.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE){
            throw new IllegalArgumentException("Vous n'avez pas ce droit.");
        } else if(tacheAEviter.getDateLimite()==null){
            throw new IllegalArgumentException("La date doit être spécifiée.");
        } else if (tacheAEviter.getDateLimite().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date limite doit être postérieure à la date actuelle.");
        } else if (tacheAEviter.getTitre()==null) {
            throw new IllegalArgumentException("Le titre doit être spécifiée.");
        }

        TacheAEviter savedTache = new TacheAEviter();
        if(tacheAEviter.getDegreUrgence()!=null){
            if(tacheAEviter.getDegreUrgence()>=1 && tacheAEviter.getDegreUrgence()<=5){
                savedTache.setDegreUrgence(tacheAEviter.getDegreUrgence());
            }else{
                throw new IllegalArgumentException("Le degré d'urgence doit être compris entre 1 et 5.");
            }
        }
        savedTache.setUtilisateur(utilisateurCourant);
        savedTache.setDateLimite(tacheAEviter.getDateLimite());
        savedTache.setTitre(tacheAEviter.getTitre());
        if(tacheAEviter.getConsequences()!=null){
            savedTache.setConsequences(tacheAEviter.getConsequences());
        }
        if(tacheAEviter.getDescription()!=null){
            savedTache.setDescription(tacheAEviter.getDescription());
        }
        return repositoryTacheAEviter.save(savedTache);
    }

    public TacheAEviter setStatut(Long idTache, StatutTache statut){
        Utilisateur utilisateurCourant = serviceUtilisateur.getUtilisateurCourant();
        Utilisateur utilisateur = repositoryUtilisateur.findById(utilisateurCourant.getId()).get();
        List<TacheAEviter> taches = utilisateur.getTaches();
        for(TacheAEviter tache : taches){
            if (tache.getId()==idTache){
                tache.setStatut(statut);
                return repositoryTacheAEviter.save(tache);
            }
        }
        throw new IllegalArgumentException("Vous n'êtes pas le non-réalisateur de cette tâche.");
    }

    public Integer computePointsRapportes(TacheAEviter tacheAEviter, LocalDate dateDuCalcul){
        LocalDate dateLimite = tacheAEviter.getDateLimite();
        int joursEntre = (int) ChronoUnit.DAYS.between(dateLimite, dateDuCalcul);

        int points = tacheAEviter.getDegreUrgence()*10 + 5*(joursEntre);
        return Integer.min(200, points);
    }
}
