package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.services.ServiceRecompense;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/recompenses")
public class ControllerRecompense {

    private final ServiceRecompense service;

    public ControllerRecompense(ServiceRecompense service) {
        this.service = service;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> creer(@RequestBody Recompense recompense) {
        try {
            Recompense nouvelleRecompense = service.creerRecompense(
                    recompense.getTitre(),
                    recompense.getDescription(),
                    recompense.getConditionsObtention(),
                    recompense.getNiveau(),
                    recompense.getType()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleRecompense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }
}