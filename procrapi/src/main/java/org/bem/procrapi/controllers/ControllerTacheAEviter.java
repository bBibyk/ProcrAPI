package org.bem.procrapi.controllers;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.services.ServiceTacheAEviter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if(UtilisateurHolder.getCurrentUser()==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentifiez-vous avant d'utiliser cette méthode.");
        }
        Optional<TacheAEviter> createdTache = serviceTacheAEviter.create(tacheAEviter);
        if(createdTache.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTache.get());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Cette tâche ne peut pas être crée.");
    }
}
