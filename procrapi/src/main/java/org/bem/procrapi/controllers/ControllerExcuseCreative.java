package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.services.ServiceExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportExcuseCreative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/excuse")
public class ControllerExcuseCreative {

    private final ServiceExcuseCreative serviceExcuseCreative;

    @Autowired
    public ControllerExcuseCreative(ServiceExcuseCreative serviceExcuseCreative) {
        this.serviceExcuseCreative = serviceExcuseCreative;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestBody ImportExcuseCreative excuseCreative) {
        try {
            ExcuseCreative createdExcuse = serviceExcuseCreative.create(
                    excuseCreative.getTexte(),
                    excuseCreative.getSituation(),
                    excuseCreative.getVotesRecus(),
                    excuseCreative.getCategorie()
            );
            //cas normal
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExcuse);
        } catch (Exception e) {
            //cas d'exception prévue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
