package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.services.ServiceRecompense;
import org.bem.procrapi.utilities.dto.ImportRecompense;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer les récompenses.
 */
@RestController
@RequestMapping(path = "/api/recompense")
public class ControllerRecompense {

    private final ServiceRecompense serviceRecompense;

    @Autowired
    public ControllerRecompense(ServiceRecompense serviceRecompense) {
        this.serviceRecompense = serviceRecompense;
    }

    /**
     * Crée une nouvelle récompense.
     * @param recompense DTO contenant les informations nécessaires
     * @return ResponseEntity avec la récompense créée ou un message d'erreur
     */
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportRecompense recompense) {
        try {
            Recompense createdRecompense = serviceRecompense.create(
                    recompense.getTitre(),
                    recompense.getDescription(),
                    recompense.getConditionsObtention(),
                    recompense.getNiveau(),
                    recompense.getType()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecompense);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceRecompense.getAll());
    }
}
