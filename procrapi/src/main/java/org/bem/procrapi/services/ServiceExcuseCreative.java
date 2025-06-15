package org.bem.procrapi.services;


import org.bem.procrapi.components.authentication.EmailHolder;
import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.bem.procrapi.utilities.enumerations.StatutTache;
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

    public ExcuseCreative create(String texte, String situation, int votesRecus,
                                 LocalDate dateSoumission, CategorieExcuse categorie) {

        Utilisateur currentUser = serviceUtilisateur.getUtilisateurCourant();

        List<TacheAEviter> tachesSucces = repositoryTacheAEviter.findByUtilisateurIdAndStatut(
                currentUser.getId(), StatutTache.EVITE_AVEC_SUCCES
        );

        List<TacheAEviter> tachesCatastrophe = repositoryTacheAEviter.findByUtilisateurIdAndStatut(
                currentUser.getId(), StatutTache.CATASTROPHE
        );

        if (tachesSucces.isEmpty() && tachesCatastrophe.isEmpty()) {
            throw new IllegalArgumentException("Vous n'avez pas de tâche éligible pour soumettre une excuse.");
        }


        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexte(texte);
        excuse.setSituation(situation);
        excuse.setVotesRecus(votesRecus);
        excuse.setCreateur(currentUser);
        // TODO : est-ce vraiment nécessaire de laisser la possibiliter à l'utilisateur de saisir la date ?
        excuse.setDateSoumission(dateSoumission != null ? dateSoumission : LocalDate.now());
        excuse.setCategorie(categorie);
        excuse.setStatut(StatutExcuse.EN_ATTENTE);

        return repositoryExcuseCreative.save(excuse);
    }

    // TODO ajouter le controlleur associé à cette méthode
    public List<ExcuseCreative> getStatutEnAttente() {
        return repositoryExcuseCreative.findByStatut(StatutExcuse.EN_ATTENTE);
    }

    // TODO : compléter avec une méthode pour setStatut
    // seul le gestionnaire du temps peut l'appeler




    // TODO ajouter une méthode pour voter pour une excuse créative
    // Expliquer que c'est un bonus qu'on a décidé d'implémenter
    // Seuls les utilisateur peuvent voter
    // Seules les excuses avec statut aprouvé peuvent être votées

}



