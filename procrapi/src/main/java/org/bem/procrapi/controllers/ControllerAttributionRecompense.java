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

@RestController
@RequestMapping(path = "/api/attributions")
public class ControllerAttributionRecompense {

    private final ServiceAttributionRecompense serviceAttributionRecompense;

    @Autowired
    public ControllerAttributionRecompense(ServiceAttributionRecompense serviceAttributionRecompense) {
        this.serviceAttributionRecompense = serviceAttributionRecompense;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportAttributionRecompense attributionRecompense) {
        try {
            //cas normal
            AttributionRecompense createdAttribution = serviceAttributionRecompense.create(
                    attributionRecompense.getUtilisateur(),
                    attributionRecompense.getRecompense(),
                    attributionRecompense.getContexteAttribution(),
                    attributionRecompense.getDateExpiration()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAttribution);
        } catch (ServiceValidationException e) {
            //cas d'ServiceValidationException prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
