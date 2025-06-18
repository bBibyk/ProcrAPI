package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.services.ServiceParticipationDefi;
import org.bem.procrapi.utilities.dto.ImportParticipationDefi;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour gérer les participations aux défis de procrastination.
 */
@RestController
@RequestMapping("/api/participationdefi")
public class ControllerParticipationDefi {

    private final ServiceParticipationDefi serviceParticipation;

    @Autowired
    public ControllerParticipationDefi(ServiceParticipationDefi serviceParticipation) {
        this.serviceParticipation = serviceParticipation;
    }

    /**
     * Crée une nouvelle participation à un défi.
     * @param participationDefi DTO contenant les informations nécessaires
     * @return ResponseEntity avec la participation créée ou un message d'erreur
     */
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportParticipationDefi participationDefi) {
        try {
            ParticipationDefi savedParticipation = serviceParticipation.create(
                    participationDefi.getDefi() == null ? null : participationDefi.getDefi().getTitre()
            );
            return new ResponseEntity<>(savedParticipation, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
