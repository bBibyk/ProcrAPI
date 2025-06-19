package org.bem.procrapi.services;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
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
            throw new ServiceValidationException("Vous n'avez pas ce droit.");
        } else if(dateLimite==null){
            throw new ServiceValidationException("La date doit être spécifiée.");
        } else if (dateLimite.isBefore(LocalDate.now())) {
            throw new ServiceValidationException("La date limite doit être postérieure à la date actuelle.");
        } else if (titre==null) {
            throw new ServiceValidationException("Le titre doit être spécifiée.");
        } else if (repositoryTacheAEviter.findByTitre(titre).isPresent()) {
            throw new ServiceValidationException("Cette tâche existe déjà.");
        }

        TacheAEviter savedTache = new TacheAEviter();
        if(degreUrgence!=null){
            if(degreUrgence>=1 && degreUrgence<=5){
                savedTache.setDegreUrgence(degreUrgence);
            }else{
                throw new ServiceValidationException("Le degré d'urgence doit être compris entre 1 et 5.");
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

    public TacheAEviter setStatut(String titreTache, StatutTache statut){
        Utilisateur utilisateurCourant = serviceUtilisateur.getUtilisateurCourant();
        Utilisateur utilisateur = repositoryUtilisateur.findById(utilisateurCourant.getId()).get();
        List<TacheAEviter> taches = utilisateur.getTaches();
        for(TacheAEviter tache : taches){
            if (tache.getTitre().equals(titreTache)){
                tache.setStatut(statut);
                if (statut == StatutTache.EVITE_AVEC_SUCCES) {
                   List<TacheAEviter> tachesCompleteesCeMois=repositoryTacheAEviter.findByUtilisateurIdAndDateCompletionBetween(utilisateur.getId(), LocalDate.now(), LocalDate.now().minusMonths(1));
                   int nbTachesCompletees=tachesCompleteesCeMois.size();
                   if (nbTachesCompletees > 5) {
                      serviceUtilisateur.perdreNiveau(utilisateur);
                   }
                } else {
                    tache.setDateCompletion(LocalDate.now());
                }
                return repositoryTacheAEviter.save(tache);
            }
        }
        throw new ServiceValidationException("La tâche n'a pas été trouvée parmis vos tâches.");
    }

    protected Integer computePointsRapportes(TacheAEviter tacheAEviter, LocalDate dateDuCalcul){
        LocalDate dateLimite = tacheAEviter.getDateLimite();
        int joursEntre = (int) ChronoUnit.DAYS.between(dateLimite, dateDuCalcul);

        int points = tacheAEviter.getDegreUrgence()*10 + 5*(joursEntre);
        return Integer.min(200, points);
    }

    /**
     * Récupère la liste de toutes les tâches à éviter enregistrées en base de données.
     *
     * @return une liste de {@link TacheAEviter} représentant toutes les entités stockées.
     */
    public List<TacheAEviter> getAll() {
        return repositoryTacheAEviter.findAll();
    }
}
