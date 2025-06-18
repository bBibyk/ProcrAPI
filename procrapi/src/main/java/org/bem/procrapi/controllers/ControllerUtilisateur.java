package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.services.ServiceUtilisateur;
import org.bem.procrapi.utilities.dto.ImportUtilisateur;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour gérer les utilisateurs.
 */
@RestController
@RequestMapping(path = "/api/utilisateur")
public class ControllerUtilisateur {

    private final ServiceUtilisateur serviceUtilisateur;

    @Autowired
    public ControllerUtilisateur(ServiceUtilisateur serviceUtilisateur) {
        this.serviceUtilisateur = serviceUtilisateur;
    }

    /**
     * Crée un nouvel utilisateur.
     * @param utilisateur DTO contenant les informations nécessaires
     * @return ResponseEntity avec l'utilisateur créé ou un message d'erreur
     */
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportUtilisateur utilisateur) {
        try {
            Utilisateur createdUtilisateur = serviceUtilisateur.create(
                    utilisateur.getRole(),
                    utilisateur.getPseudo(),
                    utilisateur.getEmail(),
                    utilisateur.getExcusePreferee() == null ? null : utilisateur.getExcusePreferee().getTexte()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUtilisateur);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
