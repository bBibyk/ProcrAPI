package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.services.ServiceDefiDeProcrastination;
import org.bem.procrapi.utilities.dto.ImportDefiDeProcrastination;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur pour gérer les défis de procrastination.
 */
@Controller
@RequestMapping(path = "/api/defideprocrastination")
public class ControllerDefiDeProcrastination {

    private ServiceDefiDeProcrastination serviceDefiDeProcrastination;

    @Autowired
    public ControllerDefiDeProcrastination(ServiceDefiDeProcrastination serviceDefiDeProcrastination) {
        this.serviceDefiDeProcrastination = serviceDefiDeProcrastination;
    }

    /**
     * Crée un nouveau défi de procrastination.
     * @param defiDeProcrastination DTO contenant les informations nécessaires
     * @return ResponseEntity avec le défi créé ou un message d'erreur
     */
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportDefiDeProcrastination defiDeProcrastination) {
        try {
            DefiDeProcrastination nouveauDefi = serviceDefiDeProcrastination.create(
                    defiDeProcrastination.getDateDebut(),
                    defiDeProcrastination.getTitre(),
                    defiDeProcrastination.getDuree(),
                    defiDeProcrastination.getPointsAGagner(),
                    defiDeProcrastination.getDescription(),
                    defiDeProcrastination.getDifficulte()
            );
            return new ResponseEntity<>(nouveauDefi, HttpStatus.CREATED);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceDefiDeProcrastination.getAll());
    }
}
