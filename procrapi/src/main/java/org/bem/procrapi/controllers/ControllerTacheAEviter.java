package org.bem.procrapi.controllers;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.services.ServiceTacheAEviter;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/tacheaeviter")
public class ControllerTacheAEviter {
    private ServiceTacheAEviter serviceTacheAEviter;

    @Autowired
    public ControllerTacheAEviter(ServiceTacheAEviter serviceTacheAEviter) {
        this.serviceTacheAEviter = serviceTacheAEviter;
    }

    @PostMapping(path="/create")
    public ResponseEntity<?> create(@RequestBody TacheAEviter tacheAEviter) {
        try{
            TacheAEviter nouvelleTache = serviceTacheAEviter.create(tacheAEviter);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleTache);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping(path="/setStatut/{idTache}")
    public ResponseEntity<?> setStatut(@PathVariable Long idTache, @RequestBody StatutTache statut) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(serviceTacheAEviter.setStatut(idTache, statut));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
