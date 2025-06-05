package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.services.ServiceParticipationDefi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/create/{idUtilisateur}/{idDefi}")
    public ResponseEntity<?> create(
            @PathVariable Long idUtilisateur,
            @PathVariable Long idDefi
    ) {
        try {
            ParticipationDefi participation = serviceParticipation.create(idUtilisateur, idDefi);
            return ResponseEntity.status(HttpStatus.CREATED).body(participation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
