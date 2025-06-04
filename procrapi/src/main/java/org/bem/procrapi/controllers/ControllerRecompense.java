package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.services.ServiceRecompense;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recompenses")
public class ControllerRecompense {

    private final ServiceRecompense service;

    public ControllerRecompense(ServiceRecompense service) {
        this.service = service;
    }

    @PostMapping(path = "/create")
    public Recompense creer(@RequestBody Recompense recompense) {
        return service.creerRecompense(
                recompense.getTitre(),
                recompense.getDescription(),
                recompense.getConditionsObtention(),
                recompense.getNiveau(),
                recompense.getType()
        );
    }
}