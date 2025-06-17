package org.bem.procrapi.controllers;

import org.bem.procrapi.services.ServiceVerificationPeriodique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
// Cette classe permet de manuellement triggerer les méthodes temporelles
// Logiquement la function PUT a été choisis, car on change des entités
public class ControllerVerificationPeriodiqueTest {

    private final ServiceVerificationPeriodique service;

    @Autowired
    public ControllerVerificationPeriodiqueTest(ServiceVerificationPeriodique service) {
        this.service = service;
    }

    @PutMapping("/reevaluer-points-taches-evitees")
    public String triggerReevaluerPointsTachesEvitees() {
        service.reevaluerPointsTachesEvitees();
        return "Réévaluation des points terminée.";
    }

    @PutMapping("/commencer-defis")
    public String triggerCommencerDefis() {
        service.commencerDefis();
        return "Défis commencés.";
    }

    @PutMapping("/terminer-defis")
    public String triggerTerminerDefis() {
        service.terminerDefis();
        return "Défis terminés.";
    }

    @PutMapping("/terminer-recompenses")
    public String triggerTerminerRecompenses() {
        service.terminerAttributionsRecompensesExpires();
        return "Récompenses expirées traitées.";
    }

    @PutMapping("/reinitialiser-votes")
    public String reinitialiserVotesExcusesValide() {
        service.reinitialiserVotesExcusesValide();
        return "Excuses réinitialisées.";
    }
}
