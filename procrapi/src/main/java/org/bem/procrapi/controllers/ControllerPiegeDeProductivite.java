package org.bem.procrapi.controllers;


import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.services.ServicePiegeDeProductivite;
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
    public ResponseEntity<?> creerPiege(@RequestBody PiegeDeProductivite piege) {
        try {
            PiegeDeProductivite created = piegeService.create(piege);
            //cas normal
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            //cas d'exception prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
