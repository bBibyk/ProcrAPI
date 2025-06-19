package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.services.ServicePiegeDeProductivite;
import org.bem.procrapi.utilities.dto.ImportPiegeDeProductivite;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Contrôleur pour gérer les pièges de productivité.
 */
@RestController
@RequestMapping("/api/piegedeproductivite")
public class ControllerPiegeDeProductivite {

    private final ServicePiegeDeProductivite piegeService;

    public ControllerPiegeDeProductivite(ServicePiegeDeProductivite piegeService) {
        this.piegeService = piegeService;
    }

    /**
     * Crée un nouveau piège de productivité.
     * @param piege DTO contenant les informations nécessaires
     * @return ResponseEntity avec le piège créé ou un message d'erreur
     */
    @PostMapping(path = "/creer")
    public ResponseEntity<?> creerPiege(@RequestBody ImportPiegeDeProductivite piege) {
        try {
            PiegeDeProductivite created = piegeService.create(
                    piege.getTitre(),
                    piege.getType(),
                    piege.getDifficulte(),
                    piege.getDescription(),
                    piege.getRecompense() == null ? null : piege.getRecompense().getTitre(),
                    piege.getConsequence()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ServiceValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path="/getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(piegeService.getAll());
    }
}
