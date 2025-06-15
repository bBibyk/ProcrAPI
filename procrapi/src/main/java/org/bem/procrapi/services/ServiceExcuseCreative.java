package org.bem.procrapi.services;


import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceExcuseCreative {

    private final RepositoryExcuseCreative repositoryExcuseCreative;
    private final ServiceUtilisateur serviceUtilisateur;
    private final RepositoryTacheAEviter repositoryTacheAEviter;

    @Autowired
    public ServiceExcuseCreative(RepositoryExcuseCreative repositoryExcuseCreative,
                                 RepositoryTacheAEviter repositoryTacheAEviter,
                                 ServiceUtilisateur serviceUtilisateur) {
        this.repositoryExcuseCreative = repositoryExcuseCreative;
        this.repositoryTacheAEviter = repositoryTacheAEviter;
        this.serviceUtilisateur = serviceUtilisateur;
    }

    //TODO votes recus doit être systématiquement à 0 !
    public ExcuseCreative create(String texte, String situation, Integer votesRecus, CategorieExcuse categorie) {

        Utilisateur currentUser = serviceUtilisateur.getUtilisateurCourant();

        List<TacheAEviter> tachesSucces = repositoryTacheAEviter.findByUtilisateurIdAndStatut(
                currentUser.getId(), StatutTache.EVITE_AVEC_SUCCES
        );

        List<TacheAEviter> tachesCatastrophe = repositoryTacheAEviter.findByUtilisateurIdAndStatut(
                currentUser.getId(), StatutTache.CATASTROPHE
        );

        if (tachesSucces.isEmpty() && tachesCatastrophe.isEmpty()) {
            throw new ServiceValidationException("Vous n'avez pas de tâche éligible pour soumettre une excuse.");
        }


        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexte(texte);
        excuse.setSituation(situation);
        excuse.setVotesRecus(votesRecus);
        excuse.setCreateur(currentUser);
        excuse.setDateSoumission(LocalDate.now());
        excuse.setCategorie(categorie);
        excuse.setStatut(StatutExcuse.EN_ATTENTE);

        return repositoryExcuseCreative.save(excuse);
    }

    public List<ExcuseCreative> getExusesByStatut(StatutExcuse statut) {
        return repositoryExcuseCreative.findByStatut(statut);
    } //TODO controller


    public ExcuseCreative setStatut(Long idExcuse, StatutExcuse nouveauStatut) {
        Utilisateur current = serviceUtilisateur.getUtilisateurCourant();

        if (current.getRole() != RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU) {
            throw new ServiceValidationException("Seul le Gestionnaire du Temps Perdu peut approuver une excuse.");
        }

        ExcuseCreative excuse = repositoryExcuseCreative.findById(idExcuse)
                .orElseThrow(() -> new ServiceValidationException("Excuse non trouvée."));

        excuse.setStatut(nouveauStatut);
        return repositoryExcuseCreative.save(excuse);
    } //TODO controller

    public ExcuseCreative voterPourExcuse(Long idExcuse) {
        Utilisateur current = serviceUtilisateur.getUtilisateurCourant();

        if (current.getRole() == RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU) {
            throw new ServiceValidationException("Le Gestionnaire ne peut pas voter.");
        }

        ExcuseCreative excuse = repositoryExcuseCreative.findById(idExcuse)
                .orElseThrow(() -> new ServiceValidationException("Excuse non trouvée."));

        if (excuse.getStatut() != StatutExcuse.APPROUVEE) {
            throw new ServiceValidationException("Vous ne pouvez voter que pour une excuse approuvée.");
        }

        excuse.setVotesRecus(excuse.getVotesRecus() + 1);
        return repositoryExcuseCreative.save(excuse);
    } //TODO controller


}



