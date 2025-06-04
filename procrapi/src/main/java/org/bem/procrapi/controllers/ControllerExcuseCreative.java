package org.bem.procrapi.controllers;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.services.ServiceExcuseCreative;
import org.springframework.web.bind.annotation.*;

public class ControllerExcuseCreative {

    private final ServiceExcuseCreative service;

    public ControllerExcuseCreative(ServiceExcuseCreative service) {
        this.service = service;
    }

    @PostMapping(path="/creer")
    public ExcuseCreative creer(@RequestBody ExcuseCreative excuse) {
        return service.creerExcuse(
                excuse.getTexte(),
                excuse.getSituation(),
                excuse.getVotesRecus(),
                excuse.getCreateur(),
                excuse.getDateSoumission(),
                excuse.getCategorie()
        );
    }
}