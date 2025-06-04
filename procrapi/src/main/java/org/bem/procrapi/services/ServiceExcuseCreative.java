package org.bem.procrapi.services;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ServiceExcuseCreative {
    private final RepositoryExcuseCreative repository;
    @Autowired
    public ServiceExcuseCreative(RepositoryExcuseCreative repository) {
        this.repository = repository;
    }

    public ExcuseCreative creerExcuse(String texte, String situation, int votesRecus, Utilisateur auteur,
                                      Date dateSoumission, CategorieExcuse categorie) {
        ExcuseCreative excuse = new ExcuseCreative();
        excuse.setTexte(texte);
        excuse.setSituation(situation);
        excuse.setVotesRecus(votesRecus);
        excuse.setCreateur(auteur);
        excuse.setUtilisateurs(List.of(auteur));
        excuse.setDateSoumission(dateSoumission);
        excuse.setCategorie(categorie);
        excuse.setStatut(StatutExcuse.EN_ATTENTE);
        return repository.save(excuse);
    }


    public void supprimerExcuse(Long id) {
        repository.deleteById(id);
    }
}