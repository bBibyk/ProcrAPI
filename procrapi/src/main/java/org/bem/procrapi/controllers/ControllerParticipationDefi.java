package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.services.ServiceParticipationDefi;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/participationdefi")
public class ControllerParticipationDefi {
    private final ServiceParticipationDefi serviceParticipation;

    @Autowired
    public ControllerParticipationDefi(ServiceParticipationDefi serviceParticipation) {
        this.serviceParticipation = serviceParticipation;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ParticipationDefi participationDefi) {
        try {
            ParticipationDefi savedParticipation = serviceParticipation.create(participationDefi);
            //cas normal
            return new ResponseEntity<>(savedParticipation, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            //cas d'ServiceValidationException prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
