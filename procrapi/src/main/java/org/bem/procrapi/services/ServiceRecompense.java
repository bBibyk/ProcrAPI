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

        public Recompense creerRecompense(String titre, String description, String conditions, NiveauDePrestige niveauPrestige, TypeRecompense type) {
            Recompense recompense = new Recompense();
            recompense.setTitre(titre);
            recompense.setDescription(description);
            recompense.setConditionsObtention(conditions);
            recompense.setNiveau(niveauPrestige);
            recompense.setType(type);
            return repository.save(recompense);
        }

        public void supprimerRecompense(Long id) {
            repository.deleteById(id);
        }
    }


