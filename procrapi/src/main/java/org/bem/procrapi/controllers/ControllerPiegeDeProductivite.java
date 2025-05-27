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
    private final ServicePiegeDeProductivite servicePiegeDeProductivite;

    public ControllerPiegeDeProductivite(ServicePiegeDeProductivite piegeService, ServicePiegeDeProductivite servicePiegeDeProductivite) {
        this.piegeService = piegeService;
        this.servicePiegeDeProductivite = servicePiegeDeProductivite;
    }

    @PostMapping(path="/creer")
    public ResponseEntity<?> creerPiege(@RequestBody PiegeDeProductivite piege) {
        Optional<PiegeDeProductivite> created = servicePiegeDeProductivite.creerPiege(piege.getTitre());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }
}
