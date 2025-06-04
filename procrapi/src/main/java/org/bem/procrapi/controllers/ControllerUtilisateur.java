package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.services.ServiceUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/utilisateur")
public class ControllerUtilisateur {

    private final ServiceUtilisateur serviceUtilisateur;

    @Autowired
    public ControllerUtilisateur(ServiceUtilisateur serviceUtilisateur) {
        this.serviceUtilisateur = serviceUtilisateur;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody Utilisateur utilisateur) {
        try{
            Utilisateur createdUtilisateur = serviceUtilisateur.create(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUtilisateur);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}

