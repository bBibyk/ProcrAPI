package org.bem.procrapi.services;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryAttributionRecompense;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServiceAttributionRecompense {

    private final RepositoryAttributionRecompense repository;
    private final RepositoryUtilisateur repositoryUtilisateur;
    private final RepositoryRecompense repositoryRecompense;

    @Autowired
    public ServiceAttributionRecompense(
            RepositoryAttributionRecompense repository,
            RepositoryUtilisateur repositoryUtilisateur,
            RepositoryRecompense repositoryRecompense) {
        this.repository = repository;
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryRecompense = repositoryRecompense;
    }

    public AttributionRecompense attribuerRecompense(Utilisateur utilisateur, Recompense recompense, String contexte,LocalDate dateExpiration) {

        if (utilisateur == null || utilisateur.getId() == null) {
            throw new IllegalArgumentException("Utilisateur non valide.");
        }

        if (recompense == null || recompense.getId() == null) {
            throw new IllegalArgumentException("Récompense non valide.");
        }


        Utilisateur fullUtilisateur = repositoryUtilisateur.findById(utilisateur.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cette utilisateur n'existe pas."));

        Recompense fullRecompense = repositoryRecompense.findById(recompense.getId())
                .orElseThrow(() -> new IllegalArgumentException("Récompense introuvable."));

        if (fullRecompense.getNiveau() == NiveauDePrestige.OR) {
            if (!aAssezDAnciennete(fullUtilisateur)) {
                throw new IllegalArgumentException("Utilisateur trop récent pour recevoir une récompense de niveau OR.");
            }
            if (fullUtilisateur.getPointsAccumules() < 2000) {
                throw new IllegalArgumentException("Utilisateur n'a pas assez de points pour une récompense de niveau OR.");
            }
        }

        AttributionRecompense attribution = new AttributionRecompense();
        attribution.setUtilisateur(fullUtilisateur);
        attribution.setRecompense(fullRecompense);
        attribution.setDateObtention(LocalDate.now());
        attribution.setDateExpiration(dateExpiration);
        attribution.setContexteAttribution(contexte);
        attribution.setStatut(StatutRecompense.ACTIF);

        return repository.save(attribution);
    }

    public List<AttributionRecompense> getTypeBadge() {
        return repository.findByRecompense_Type(TypeRecompense.BADGE);
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
