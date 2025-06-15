package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.services.ServiceRecompense;
import org.bem.procrapi.utilities.dto.ImportRecompense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/recompense")
public class ControllerRecompense {

    private final ServiceRecompense serviceRecompense;

    @Autowired
    public ControllerRecompense(ServiceRecompense serviceRecompense) {
        this.serviceRecompense = serviceRecompense;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportRecompense recompense) {
        try {
            Recompense createdRecompense = serviceRecompense.create(
                    recompense.getTitre(),
                    recompense.getDescription(),
                    recompense.getConditionsObtention(),
                    recompense.getNiveau(),
                    recompense.getType());
            //cas normal
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecompense);
        } catch (Exception e) {
            //cas d'exception prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
