package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.services.ServiceUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<?> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        Optional<Utilisateur> createdUser = serviceUtilisateur.createUtilisateur(utilisateur);

        if (createdUser.isEmpty()) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity
                .created(URI.create("/api/utilisateur/" + createdUser.get().getId()))
                .body(createdUser.get());
    }
}

