package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.services.ServiceExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportExcuseCreative;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
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
                    excuseCreative.getVotesRecus(),
                    excuseCreative.getCategorie()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExcuse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Permet de récupérer les excuses en fonction de leur statut.
     * Requête : GET http://localhost:8080/api/excuse/statut/{statut}
     * @param statut le statut des excuses recherchées
     * @return liste des excuses ou une erreur
     */

    @GetMapping(path = "/statut/{statut}")
    public ResponseEntity<?> getByStatut(@PathVariable StatutExcuse statut) {
        try {
            List<ExcuseCreative> excuses = serviceExcuseCreative.getExusesByStatut(statut);
            return ResponseEntity.ok(excuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Permet au Gestionnaire du Temps Perdu de changer le statut d'une excuse.
     * Requête : PUT http://localhost:8080/api/excuse/changer-statut/{idExcuse}
     * @param idExcuse identifiant de l'excuse
     * @param nouveauStatut nouveau statut à appliquer
     * @return excuse mise à jour ou erreur
     */

    @PutMapping(path = "/changer-statut/{idExcuse}")
    public ResponseEntity<?> setStatut(@PathVariable Long idExcuse, @RequestBody StatutExcuse nouveauStatut) {
        try {
            ExcuseCreative updated = serviceExcuseCreative.setStatut(idExcuse, nouveauStatut);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Permet à un utilisateur de voter pour une excuse approuvée.
     * Requête : PUT http://localhost:8080/api/excuse/voter/{idExcuse}
     * @param idExcuse identifiant de l'excuse
     * @return excuse mise à jour ou erreur
     */

    @PutMapping(path = "/voter/{idExcuse}")
    public ResponseEntity<?> voter(@PathVariable Long idExcuse) {
        try {
            ExcuseCreative voted = serviceExcuseCreative.voterPourExcuse(idExcuse);
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
