package org.bem.procrapi.controllers;


import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.services.ServicePiegeDeProductivite;
import org.bem.procrapi.utilities.dto.ImportPiegeDeProductivite;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/piegedeproductivite")
public class ControllerPiegeDeProductivite {

    private final ServicePiegeDeProductivite piegeService;

    public ControllerPiegeDeProductivite(ServicePiegeDeProductivite piegeService) {
        this.piegeService = piegeService;
    }

    @PostMapping(path="/creer")
    public ResponseEntity<?> creerPiege(@RequestBody ImportPiegeDeProductivite piege) {
        try {
            PiegeDeProductivite created = piegeService.create(
                    piege.getTitre(),
                    piege.getType(),
                    piege.getDifficulte(),
                    piege.getDescription(),
                    piege.getRecompense()==null ? null : piege.getRecompense().getTitre(),
                    piege.getConsequence());
            //cas normal
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ServiceValidationException e) {
            //cas d'ServiceValidationException prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
