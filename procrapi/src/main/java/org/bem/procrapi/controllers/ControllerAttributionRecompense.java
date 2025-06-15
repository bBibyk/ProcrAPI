package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.services.ServiceAttributionRecompense;
import org.bem.procrapi.utilities.dto.ImportAttributionRecompense;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour gérer les attributions de récompenses.
 */
@RestController
@RequestMapping(path = "/api/attributions")
public class ControllerAttributionRecompense {

    /**
     * Service métier pour l'attribution des récompenses.
     */
    private final ServiceAttributionRecompense serviceAttributionRecompense;

    /**
     * Constructeur avec injection du service.
     * @param serviceAttributionRecompense service métier injecté
     */
    @Autowired
    public ControllerAttributionRecompense(ServiceAttributionRecompense serviceAttributionRecompense) {
        this.serviceAttributionRecompense = serviceAttributionRecompense;
    }

    /**
     * Endpoint pour attribuer une récompense à un utilisateur.
     * Requête POST vers http://localhost:8080/api/attributions/create
     * @param attributionRecompense DTO contenant les informations nécessaires
     * @return l'attribution créée ou un message d'erreur
     */
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportAttributionRecompense attributionRecompense) {
        try {
            AttributionRecompense createdAttribution = serviceAttributionRecompense.create(
                    attributionRecompense.getUtilisateur(),
                    attributionRecompense.getRecompense(),
                    attributionRecompense.getContexteAttribution(),
                    attributionRecompense.getDateExpiration()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAttribution);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

