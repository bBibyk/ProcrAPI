package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.services.ServiceAttributionRecompense;
import org.bem.procrapi.utilities.dto.ImportAttributionRecompense;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer les attributions de récompenses.
 */
@RestController
@RequestMapping(path = "/api/attributions")
public class ControllerAttributionRecompense {

    private final ServiceAttributionRecompense serviceAttributionRecompense;

    @Autowired
    public ControllerAttributionRecompense(ServiceAttributionRecompense serviceAttributionRecompense) {
        this.serviceAttributionRecompense = serviceAttributionRecompense;
    }

    /**
     * Crée une attribution de récompense.
     * @param attributionRecompense DTO contenant les informations nécessaires
     * @return ResponseEntity avec l'attribution créée ou un message d'erreur
     */
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportAttributionRecompense attributionRecompense) {
        try {
            AttributionRecompense createdAttribution = serviceAttributionRecompense.create(
                    attributionRecompense.getUtilisateur().getEmail(),
                    attributionRecompense.getRecompense().getTitre(),
                    attributionRecompense.getContexteAttribution(),
                    attributionRecompense.getDateExpiration()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAttribution);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceAttributionRecompense.getAll());
    }
}
