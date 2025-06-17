package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.services.ServiceRecompense;
import org.bem.procrapi.utilities.dto.ImportRecompense;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/recompense")
public class ControllerRecompenseTest {

    private final ServiceRecompense serviceRecompense;

    @Autowired
    public ControllerRecompenseTest(ServiceRecompense serviceRecompense) {
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
        } catch (ServiceValidationException e) {
            //cas d'ServiceValidationException prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
