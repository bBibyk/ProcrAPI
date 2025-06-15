package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.services.ServiceTacheAEviter;
import org.bem.procrapi.utilities.dto.ImportTacheAEviter;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/tacheaeviter")
public class ControllerTacheAEviter {
    private ServiceTacheAEviter serviceTacheAEviter;

    @Autowired
    public ControllerTacheAEviter(ServiceTacheAEviter serviceTacheAEviter) {
        this.serviceTacheAEviter = serviceTacheAEviter;
    }

    @PostMapping(path="/create")
    public ResponseEntity<?> create(@RequestBody ImportTacheAEviter tacheAEviter) {
        try{
            TacheAEviter nouvelleTache = serviceTacheAEviter.create(
                    tacheAEviter.getDateLimite(),
                    tacheAEviter.getTitre(),
                    tacheAEviter.getDegreUrgence(),
                    tacheAEviter.getConsequences(),
                    tacheAEviter.getDescription());
            //cas normal
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleTache);
        } catch (ServiceValidationException e) {
            //cas d'ServiceValidationException prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path="/setStatut")
    public ResponseEntity<?> setStatut(@RequestBody String titreTache, @RequestBody StatutTache statut) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(serviceTacheAEviter.setStatut(titreTache, statut));
        }catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
