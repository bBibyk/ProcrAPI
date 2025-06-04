package org.bem.procrapi.services;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryAttributionRecompense;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class ServiceAttributionRecompense {
    private final RepositoryAttributionRecompense repository;
    @Autowired
    public ServiceAttributionRecompense(RepositoryAttributionRecompense repository) {
        this.repository = repository;
    }

    public AttributionRecompense attribuerRecompense(Utilisateur utilisateur, Recompense recompense , String contexte) {
        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setUtilisateur(utilisateur);
        attribution.setRecompense(recompense);
        attribution.setDateObtention(new Date());
        attribution.setDateExpiration(null);
        attribution.setContexteAttribution(contexte);
        attribution.setStatut(StatutRecompense.ACTIF);
        return repository.save(attribution);
    }

    public void supprimerAttribution(Long id) {
        repository.deleteById(id);
    }
}