package org.bem.procrapi.services;

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

    public TacheAEviter create(LocalDate dateLimite,
                               String titre,
                               Integer degreUrgence,
                               String consequences,
                               String description){
        Utilisateur utilisateurCourant = serviceUtilisateur.getUtilisateurCourant();
        if (utilisateurCourant.getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE){
            throw new IllegalArgumentException("Vous n'avez pas ce droit.");
        } else if(dateLimite==null){
            throw new IllegalArgumentException("La date doit être spécifiée.");
        } else if (dateLimite.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date limite doit être postérieure à la date actuelle.");
        } else if (titre==null) {
            throw new IllegalArgumentException("Le titre doit être spécifiée.");
        }

        TacheAEviter savedTache = new TacheAEviter();
        if(degreUrgence!=null){
            if(degreUrgence>=1 && degreUrgence<=5){
                savedTache.setDegreUrgence(degreUrgence);
            }else{
                throw new IllegalArgumentException("Le degré d'urgence doit être compris entre 1 et 5.");
            }
        }
        savedTache.setUtilisateur(utilisateurCourant);
        savedTache.setDateLimite(dateLimite);
        savedTache.setTitre(titre);
        if(consequences!=null){
            savedTache.setConsequences(consequences);
        }
        if(description!=null){
            savedTache.setDescription(description);
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

    protected Integer computePointsRapportes(TacheAEviter tacheAEviter, LocalDate dateDuCalcul){
        LocalDate dateLimite = tacheAEviter.getDateLimite();
        int joursEntre = (int) ChronoUnit.DAYS.between(dateLimite, dateDuCalcul);

        int points = tacheAEviter.getDegreUrgence()*10 + 5*(joursEntre);
        return Integer.min(200, points);
    }
}
