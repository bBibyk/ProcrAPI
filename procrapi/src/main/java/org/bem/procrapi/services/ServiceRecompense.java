package org.bem.procrapi.services;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceRecompense {

    private final RepositoryRecompense repository;

    @Autowired
    public ServiceRecompense(RepositoryRecompense repository) {
        this.repository = repository;
    }

    public Recompense create(String titre,
                             String description,
                             String conditionsObtention,
                             NiveauDePrestige niveau,
                             TypeRecompense type) throws IllegalArgumentException {

        if (titre == null) {
            throw new IllegalArgumentException("Titre de la récompense non valide.");
        }
        if (description == null ) {
            throw new IllegalArgumentException("Description de la récompense non valide.");
        }
        if (conditionsObtention == null) {
            throw new IllegalArgumentException("Conditions d'obtention non valides.");
        }

        if (niveau == null) {
            throw new IllegalArgumentException("Niveau de prestige non valide.");
        }

        if (type == null) {
            throw new IllegalArgumentException("Type de récompense non valide.");
        }
        Recompense savedRecompense = new Recompense();
        savedRecompense.setTitre(titre);
        savedRecompense.setDescription(description);
        savedRecompense.setConditionsObtention(conditionsObtention);
        savedRecompense.setNiveau(niveau);
        savedRecompense.setType(type);
        return repository.save(savedRecompense);
    }
}
