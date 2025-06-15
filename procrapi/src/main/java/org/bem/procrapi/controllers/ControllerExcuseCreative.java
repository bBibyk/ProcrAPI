package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.services.ServiceExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportExcuseCreative;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour la gestion des excuses créatives.
 */
@RestController
@RequestMapping(path = "/api/excuse")
public class ControllerExcuseCreative {

    private final ServiceExcuseCreative serviceExcuseCreative;


    @Autowired
    public ControllerExcuseCreative(ServiceExcuseCreative serviceExcuseCreative) {
        this.serviceExcuseCreative = serviceExcuseCreative;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportExcuseCreative excuseCreative) {
        try {
            ExcuseCreative createdExcuse = serviceExcuseCreative.create(
                    excuseCreative.getTexte(),
                    excuseCreative.getSituation(),
                    excuseCreative.getCategorie()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExcuse);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path = "/statut/")
    public ResponseEntity<?> getByStatut(@RequestParam StatutExcuse statut) {
        try {
            List<ExcuseCreative> excuses = serviceExcuseCreative.getExusesByStatut(statut);
            return ResponseEntity.ok(excuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping(path = "/changer-statut")
    public ResponseEntity<?> setStatut(@PathVariable String texteExcuse, @RequestBody StatutExcuse nouveauStatut) {
        try {
            ExcuseCreative updated = serviceExcuseCreative.setStatut(texteExcuse, nouveauStatut);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping(path = "/voter")
    public ResponseEntity<?> voter(@PathVariable String texteExcuse) {
        try {
            ExcuseCreative voted = serviceExcuseCreative.voterPourExcuse(texteExcuse);
            return ResponseEntity.ok(voted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/classement-hebdo")
    public ResponseEntity<?> classementHebdomadaire() {
        try {
            List<ExcuseCreative> topExcuses = serviceExcuseCreative.getClassementHebdomadaire();
            return ResponseEntity.ok(topExcuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
