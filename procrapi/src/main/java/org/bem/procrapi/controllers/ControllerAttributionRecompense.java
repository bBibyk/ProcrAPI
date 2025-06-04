package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.services.ServiceAttributionRecompense;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attribution")
public class ControllerAttributionRecompense {

    private final ServiceAttributionRecompense service;

    public ControllerAttributionRecompense(ServiceAttributionRecompense service) {
        this.service = service;
    }

    @PostMapping(path="/creer")
    public AttributionRecompense creer(@RequestBody AttributionRecompense attribution) {
        return service.attribuerRecompense(
                attribution.getUtilisateur(),
                attribution.getRecompense(),
                attribution.getContexteAttribution()
        );
    }
}