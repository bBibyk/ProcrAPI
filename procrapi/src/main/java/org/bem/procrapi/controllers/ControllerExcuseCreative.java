package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.services.ServiceExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportExcuseCreative;
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

    /**
     * Service métier pour les excuses créatives.
     */
    private final ServiceExcuseCreative serviceExcuseCreative;

    /**
     * Constructeur avec injection du service.
     * @param serviceExcuseCreative service injecté
     */
    @Autowired
    public ControllerExcuseCreative(ServiceExcuseCreative serviceExcuseCreative) {
        this.serviceExcuseCreative = serviceExcuseCreative;
    }

    /**
     * Endpoint pour soumettre une nouvelle excuse créative.
     * Requête POST vers http://localhost:8080/api/excuse/create
     * @param excuseCreative DTO contenant les données d'excuse
     * @return l'excuse créée ou un message d'erreur
     */
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
}
