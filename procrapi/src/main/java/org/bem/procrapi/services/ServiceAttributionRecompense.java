package org.bem.procrapi.services;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryAttributionRecompense;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class   ServiceAttributionRecompense {

    private final RepositoryAttributionRecompense repository;

    @Autowired
    public ServiceAttributionRecompense(RepositoryAttributionRecompense repository) {
        this.repository = repository;
    }

    public AttributionRecompense attribuerRecompense(Utilisateur utilisateur, Recompense recompense, String contexte) {

        if (utilisateur == null || utilisateur.getId() == null) {
            throw new IllegalArgumentException("Utilisateur non valide.");
        }
        if ("or".equalsIgnoreCase(String.valueOf(recompense.getNiveau()))) { //TODO on utilise des enums pour ça, pas in equalsIgnoreCase!
            if (!aAssezDAnciennete(utilisateur)) {
                throw new IllegalArgumentException("Utilisateur trop récent pour recevoir une récompense de niveau OR.");
            }
            if (utilisateur.getPointsAccumules() < 2000) {
                throw new IllegalArgumentException("Utilisateur n'a pas assez de points pour une récompense de niveau OR.");
            }
        }

        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setUtilisateur(utilisateur);
        attribution.setRecompense(recompense);
        attribution.setDateObtention(LocalDate.now());
        attribution.setDateExpiration(null);
        attribution.setContexteAttribution(contexte);
        attribution.setStatut(StatutRecompense.ACTIF);

        return repository.save(attribution);
    }
    public List<AttributionRecompense> getTypeBadge() {
        return repository.findByRecompense_Type(TypeRecompense.valueOf("badge"));
    }



    private boolean aAssezDAnciennete(Utilisateur utilisateur) {
        if (utilisateur.getDateInscription() == null) {
            return false;
        }
        LocalDate dateInscription = utilisateur.getDateInscription();
        LocalDate sixMoisAvant = LocalDate.now().minusMonths(6);
        return dateInscription.isBefore(sixMoisAvant);
    }
}

