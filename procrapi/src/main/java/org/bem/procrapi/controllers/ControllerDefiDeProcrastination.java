package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.services.ServiceDefiDeProcrastination;
import org.bem.procrapi.utilities.dto.ImportDefiDeProcrastination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/api/defideprocrastination")
public class ControllerDefiDeProcrastination {
    private ServiceDefiDeProcrastination serviceDefiDeProcrastination;

    @Autowired
    public ControllerDefiDeProcrastination(ServiceDefiDeProcrastination serviceDefiDeProcrastination) {
        this.serviceDefiDeProcrastination = serviceDefiDeProcrastination;
    }

    @PostMapping(path="/create")
    public ResponseEntity<?> create(@RequestBody ImportDefiDeProcrastination defiDeProcrastination) {
        try{
            DefiDeProcrastination nouveauDefi = serviceDefiDeProcrastination.create(
                    defiDeProcrastination.getDateDebut(),
                    defiDeProcrastination.getTitre(),
                    defiDeProcrastination.getDuree(),
                    defiDeProcrastination.getPointsAGagner(),
                    defiDeProcrastination.getDescription(),
                    defiDeProcrastination.getDifficulte());
            //cas normal
            return new ResponseEntity<>(nouveauDefi, HttpStatus.CREATED);
        } catch (Exception e) {
            //cas d'exception prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
