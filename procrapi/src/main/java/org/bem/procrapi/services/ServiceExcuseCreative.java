package org.bem.procrapi.services;


import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryTacheAEviter;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class ServiceExcuseCreative {

    private final RepositoryExcuseCreative repositoryExcuseCreative;
    private final RepositoryTacheAEviter repositoryTacheAEviter;

    @Autowired
    public ServiceExcuseCreative(RepositoryExcuseCreative repositoryExcuseCreative,
                                 RepositoryTacheAEviter repositoryTacheAEviter) {
        this.repositoryExcuseCreative = repositoryExcuseCreative;
        this.repositoryTacheAEviter = repositoryTacheAEviter;
    }

    public ExcuseCreative create(String texte, String situation, int votesRecus, Utilisateur createur,
                                 LocalDate dateSoumission, CategorieExcuse categorie) {


        if (UtilisateurHolder.getCurrentUser() == null) {
            throw new IllegalArgumentException("Vous n'êtes pas authentifié.");
        }

        // Vérifier que l'utilisateur connecté est bien celui qui crée l'excuse (sécurité)
        // TODO Logique cassée, autant de ne pas vérifier createur tout court
        if (!UtilisateurHolder.getCurrentUser().getId().equals(createur.getId())) {
            throw new IllegalArgumentException("Vous ne pouvez soumettre une excuse que pour vous-même.");
        }
        List<TacheAEviter> tachesSucces = repositoryTacheAEviter.findByUtilisateurIdAndStatut(
                createur.getId(), StatutTache.EVITE_AVEC_SUCCES
        );

        List<TacheAEviter> tachesCatastrophe = repositoryTacheAEviter.findByUtilisateurIdAndStatut(
                createur.getId(), StatutTache.CATASTROPHE
        );

        if (tachesSucces.isEmpty() && tachesCatastrophe.isEmpty()) {
            throw new IllegalArgumentException("Vous n'avez pas de tâche éligible pour soumettre une excuse.");
        }


        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexte(texte);
        excuse.setSituation(situation);
        excuse.setVotesRecus(votesRecus);
        excuse.setCreateur(createur);
        excuse.setDateSoumission(dateSoumission != null ? dateSoumission : LocalDate.now());
        excuse.setCategorie(categorie);
        excuse.setStatut(StatutExcuse.EN_ATTENTE);

        return repositoryExcuseCreative.save(excuse);
    }

    public List<ExcuseCreative> getStatutEnAttente() {
        return repositoryExcuseCreative.findByStatut(StatutExcuse.EN_ATTENTE);
    }

}



