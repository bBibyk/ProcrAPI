package org.bem.procrapi.services;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryAttributionRecompense;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.StatutRecompense;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service métier pour la gestion des attributions de récompenses aux utilisateurs.
 */
@Service
public class ServiceAttributionRecompense {

    /**
     * Repository des attributions de récompenses.
     */
    private final RepositoryAttributionRecompense repository;

    /**
     * Repository des utilisateurs.
     */
    private final RepositoryUtilisateur repositoryUtilisateur;

    /**
     * Repository des récompenses.
     */
    private final RepositoryRecompense repositoryRecompense;

    /**
     * Constructeur avec injection des dépendances.
     * @param repository repository des attributions
     * @param repositoryUtilisateur repository des utilisateurs
     * @param repositoryRecompense repository des récompenses
     */
    @Autowired
    public ServiceAttributionRecompense(
            RepositoryAttributionRecompense repository,
            RepositoryUtilisateur repositoryUtilisateur,
            RepositoryRecompense repositoryRecompense) {
        this.repository = repository;
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryRecompense = repositoryRecompense;
    }

    /**
     * Permet d’attribuer une récompense à un utilisateur, avec des règles de validation selon le niveau.
     * @param email utilisateur cible
     * @param titreRecompense récompense à attribuer
     * @param contexte contexte de l’attribution
     * @param dateExpiration date éventuelle d’expiration
     * @return l’attribution créée
     * @throws ServiceValidationException si des règles métier sont violées
     */
    public AttributionRecompense create(String email,
                                        String titreRecompense,
                                        String contexte,
                                        LocalDate dateExpiration) {

        if (email == null) {
            throw new ServiceValidationException("Utilisateur non valide.");
        }
        Utilisateur fullUtilisateur = repositoryUtilisateur.findByEmail(email)
                .orElseThrow(() -> new ServiceValidationException("Cette utilisateur n'existe pas."));

        if (titreRecompense == null) {
            throw new ServiceValidationException("Récompense non valide.");
        }
        Recompense fullRecompense = repositoryRecompense.findByTitre(titreRecompense)
                .orElseThrow(() -> new ServiceValidationException("Recompense introuvable."));

        if (fullRecompense.getNiveau() == NiveauDePrestige.OR) {
            if (!aAssezDAnciennete(fullUtilisateur)) {
                throw new ServiceValidationException("Utilisateur trop récent pour recevoir une récompense de niveau OR.");
            }
            if (fullUtilisateur.getPointsAccumules() < 2000) {
                throw new ServiceValidationException("Utilisateur n'a pas assez de points pour une récompense de niveau OR.");
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

    /**
     * Vérifie si l’utilisateur est inscrit depuis au moins 6 mois.
     * @param utilisateur utilisateur à vérifier
     * @return true s’il est assez ancien, false sinon
     */
    private boolean aAssezDAnciennete(Utilisateur utilisateur) {
        if (utilisateur.getDateInscription() == null) return false;
        return utilisateur.getDateInscription().isBefore(LocalDate.now().minusMonths(6));
    }
}
