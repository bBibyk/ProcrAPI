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

    public Recompense create(Recompense recompense) throws IllegalArgumentException {

        if (recompense.getTitre() == null) {
            throw new IllegalArgumentException("Titre de la récompense non valide.");
        }
        if (recompense.getDescription() == null ) {
            throw new IllegalArgumentException("Description de la récompense non valide.");
        }
        if (recompense.getConditionsObtention() == null) {
            throw new IllegalArgumentException("Conditions d'obtention non valides.");
        }

        if (recompense.getNiveau() == null) {
            throw new IllegalArgumentException("Niveau de prestige non valide.");
        }

        if (recompense.getType() == null) {
            throw new IllegalArgumentException("Type de récompense non valide.");
        }
        Recompense savedRecompense = new Recompense();
        savedRecompense.setTitre(recompense.getTitre());
        savedRecompense.setDescription(recompense.getDescription());
        savedRecompense.setConditionsObtention(recompense.getConditionsObtention());
        savedRecompense.setNiveau(recompense.getNiveau());
        savedRecompense.setType(recompense.getType());
        return repository.save(recompense);
    }
}
