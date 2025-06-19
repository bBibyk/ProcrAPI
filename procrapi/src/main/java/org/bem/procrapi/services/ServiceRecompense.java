package org.bem.procrapi.services;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.entities.ConfrontationPiege;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des récompenses disponibles dans le système.
 */
@Service
public class ServiceRecompense {

    /**
     * Repository des récompenses.
     */
    private final RepositoryRecompense repository;

    /**
     * Constructeur avec injection du repository.
     * @param repository le bean repository à injecter
     */
    @Autowired
    public ServiceRecompense(RepositoryRecompense repository) {
        this.repository = repository;
    }

    /**
     * Crée une nouvelle récompense si tous les champs obligatoires sont valides.
     * De même que pour attribution, le sujet ne précise rien, donc tout le monde peut créer des récompenses.
     * @param titre titre de la récompense
     * @param description description de la récompense
     * @param conditionsObtention conditions d'obtention
     * @param niveau niveau de prestige (BRONZE, ARGENT, OR...)
     * @param type type de récompense (BADGE, etc.)
     * @return la récompense créée
     * @throws ServiceValidationException si des champs sont manquants ou invalides
     */
    public Recompense create(String titre,
                             String description,
                             String conditionsObtention,
                             NiveauDePrestige niveau,
                             TypeRecompense type) throws ServiceValidationException {

        if (titre == null) throw new ServiceValidationException("Titre de la récompense non valide.");
        if (repository.findByTitre(titre).isPresent()) throw new ServiceValidationException("Cette récompense existe déjà");
        if (description == null) throw new ServiceValidationException("Description de la récompense non valide.");
        if (conditionsObtention == null) throw new ServiceValidationException("Conditions d'obtention non valides.");
        if (niveau == null) throw new ServiceValidationException("Niveau de prestige non valide.");
        if (type == null) throw new ServiceValidationException("Type de récompense non valide.");

        Recompense savedRecompense = new Recompense();
        savedRecompense.setTitre(titre);
        savedRecompense.setDescription(description);
        savedRecompense.setConditionsObtention(conditionsObtention);
        savedRecompense.setNiveau(niveau);
        savedRecompense.setType(type);

        return repository.save(savedRecompense);
    }

    /**
     * Récupère la liste de toutes les récompenses enregistrées en base de données.
     *
     * @return une liste de {@link Recompense} représentant toutes les entités stockées.
     */
    public List<Recompense> getAll() {
        return repository.findAll();
    }
}
