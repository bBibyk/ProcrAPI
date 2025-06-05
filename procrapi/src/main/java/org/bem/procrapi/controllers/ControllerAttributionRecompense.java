package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.services.ServiceAttributionRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/attributions")
public class ControllerAttributionRecompense {

    private final ServiceAttributionRecompense serviceAttributionRecompense;

    @Autowired
    public ControllerAttributionRecompense(ServiceAttributionRecompense serviceAttributionRecompense) {
        this.serviceAttributionRecompense = serviceAttributionRecompense;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody AttributionRecompense attributionRecompense) {
        try {
            AttributionRecompense createdAttribution = serviceAttributionRecompense.attribuerRecompense(
                    attributionRecompense.getUtilisateur(),
                    attributionRecompense.getRecompense(),
                    attributionRecompense.getContexteAttribution()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAttribution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


}
