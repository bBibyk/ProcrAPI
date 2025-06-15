package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ConfrontationPiege;
import org.bem.procrapi.services.ServiceConfrontationPiege;
import org.bem.procrapi.utilities.dto.ImportConfrontationPiege;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/confrontationpiege")
public class ControllerConfrontationPiege {

    private final ServiceConfrontationPiege serviceConfrontationPiege;

    @Autowired
    public ControllerConfrontationPiege(ServiceConfrontationPiege serviceConfrontationPiege) {
        this.serviceConfrontationPiege = serviceConfrontationPiege;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createConfrontation(@RequestBody ImportConfrontationPiege confrontation) {
        try {
            ConfrontationPiege saved = serviceConfrontationPiege.create(
                    confrontation.getPiege(),
                    confrontation.getResultat(),
                    confrontation.getPoints()
            );
            //cas normal
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            //cas d'ServiceValidationException prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}