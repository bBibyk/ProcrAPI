package org.bem.procrapi.services;

import jakarta.transaction.Transactional;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ServiceTacheAEviter {
    private final ServiceUtilisateur serviceUtilisateur;
    private RepositoryTacheAEviter repositoryTacheAEviter;
    private RepositoryUtilisateur repositoryUtilisateur;

    @Autowired
    public ServiceTacheAEviter(RepositoryTacheAEviter repositoryTacheAEviter,
                               RepositoryUtilisateur repositoryUtilisateur, ServiceUtilisateur serviceUtilisateur) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryTacheAEviter = repositoryTacheAEviter;
        this.serviceUtilisateur = serviceUtilisateur;
    }

    public TacheAEviter create(TacheAEviter tacheAEviter){
        if (UtilisateurHolder.getCurrentUser() == null){
            throw new IllegalArgumentException("Vous n'êtes pas authentifié");
        }else if (UtilisateurHolder.getCurrentUser().getRole() != RoleUtilisateur.PROCRASTINATEUR_EN_HERBE){
            throw new IllegalArgumentException("Vous n'avez pas ce droit.");
        } else if(tacheAEviter.getDateLimite()==null){
            throw new IllegalArgumentException("La date doit être spécifiée.");
        } else if (tacheAEviter.getDateLimite().before(new Date())) {
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
        savedTache.setUtilisateur(UtilisateurHolder.getCurrentUser());
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

    public TacheAEviter setStatut(TacheAEviter tacheAEviter, StatutTache statut){
        tacheAEviter.setStatut(statut);
        return repositoryTacheAEviter.save(tacheAEviter);
    }
}
