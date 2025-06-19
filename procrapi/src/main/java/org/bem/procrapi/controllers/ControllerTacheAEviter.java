package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.services.ServiceTacheAEviter;
import org.bem.procrapi.utilities.dto.ImportSetStatutTache;
import org.bem.procrapi.utilities.dto.ImportTacheAEviter;
import org.bem.procrapi.utilities.enumerations.StatutTache;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer les tâches à éviter.
 */
@RestController
@RequestMapping(path = "/api/tacheaeviter")
public class ControllerTacheAEviter {

    private ServiceTacheAEviter serviceTacheAEviter;

    @Autowired
    public ControllerTacheAEviter(ServiceTacheAEviter serviceTacheAEviter) {
        this.serviceTacheAEviter = serviceTacheAEviter;
    }

    /**
     * Crée une nouvelle tâche à éviter.
     * @param tacheAEviter DTO contenant les informations nécessaires
     * @return ResponseEntity avec la tâche créée ou un message d'erreur
     */
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportTacheAEviter tacheAEviter) {
        try {
            TacheAEviter nouvelleTache = serviceTacheAEviter.create(
                    tacheAEviter.getDateLimite(),
                    tacheAEviter.getTitre(),
                    tacheAEviter.getDegreUrgence(),
                    tacheAEviter.getConsequences(),
                    tacheAEviter.getDescription()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleTache);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Met à jour le statut d'une tâche.
     * @param setStatutTache DTO contenant le titre de la tâche et le nouveau statut
     * @return ResponseEntity avec la tâche mise à jour ou un message d'erreur
     */
    @PutMapping(path = "/set-statut")
    public ResponseEntity<?> setStatut(@RequestBody ImportSetStatutTache setStatutTache) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    serviceTacheAEviter.setStatut(setStatutTache.getTitreTache(), setStatutTache.getStatut())
            );
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceTacheAEviter.getAll());
    }
}
