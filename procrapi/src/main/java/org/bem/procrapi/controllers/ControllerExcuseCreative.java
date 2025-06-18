package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.services.ServiceExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportSetStatutExcuse;
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

    /**
     * Crée une nouvelle excuse créative.
     * @param excuseCreative DTO contenant les informations nécessaires
     * @return ResponseEntity avec l'excuse créée ou un message d'erreur
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

    /**
     * Récupère les excuses par statut.
     * @param setStatutExcuse DTO contenant le statut des excuses à récupérer
     * @return ResponseEntity avec la liste des excuses
     */
    @GetMapping(path = "/get-by-statut")
    public ResponseEntity<?> getByStatut(@RequestBody ImportSetStatutExcuse setStatutExcuse) {
        List<ExcuseCreative> excuses = serviceExcuseCreative.getExusesByStatut(setStatutExcuse.getStatut());
        return ResponseEntity.ok(excuses);
    }

    /**
     * Met à jour le statut d'une excuse.
     * @param setStatutExcuse DTO contenant le texte de l'excuse et le nouveau statut
     * @return ResponseEntity avec l'excuse mise à jour ou un message d'erreur
     */
    @PutMapping(path = "/changer-statut")
    public ResponseEntity<?> setStatut(@RequestBody ImportSetStatutExcuse setStatutExcuse) {
        try {
            ExcuseCreative updated = serviceExcuseCreative.setStatut(
                    setStatutExcuse.getTexteExcuse(), setStatutExcuse.getStatut());
            return ResponseEntity.ok(updated);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Vote pour une excuse.
     * @param excuse DTO contenant le texte de l'excuse
     * @return ResponseEntity avec l'excuse votée ou un message d'erreur
     */
    @PutMapping(path = "/voter")
    public ResponseEntity<?> voter(@RequestBody ImportExcuseCreative excuse) {
        try {
            ExcuseCreative voted = serviceExcuseCreative.voterPourExcuse(excuse.getTexte());
            return ResponseEntity.ok(voted);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Récupère le classement hebdomadaire des excuses.
     * @return ResponseEntity avec la liste des excuses les mieux notées
     */
    @GetMapping("/classement-hebdo")
    public ResponseEntity<?> classementHebdomadaire() {
        List<ExcuseCreative> topExcuses = serviceExcuseCreative.getClassementHebdomadaire();
        return ResponseEntity.ok(topExcuses);
    }
}
