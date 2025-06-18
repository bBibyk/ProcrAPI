package org.bem.procrapi.controllers;

import org.bem.procrapi.services.ServiceVerificationPeriodique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour déclencher manuellement les vérifications périodiques.
 * Ce contrôleur permet de déclencher manuellement des méthodes temporelles.
 * La méthode PUT a été choisie arbitrairement car on modifie des entités.
 */
@RestController
@RequestMapping("/api/admin")
public class ControllerVerificationPeriodique {

    private final ServiceVerificationPeriodique service;

    @Autowired
    public ControllerVerificationPeriodique(ServiceVerificationPeriodique service) {
        this.service = service;
    }

    /**
     * Déclenche la réévaluation des points pour les tâches évitées.
     * @return Message de confirmation
     */
    @PutMapping("/reevaluer-points-taches-evitees")
    public String triggerReevaluerPointsTachesEvitees() {
        service.reevaluerPointsTachesEvitees();
        return "Réévaluation des points terminée.";
    }

    /**
     * Déclenche le commencement des défis.
     * @return Message de confirmation
     */
    @PutMapping("/commencer-defis")
    public String triggerCommencerDefis() {
        service.commencerDefis();
        return "Défis commencés.";
    }

    /**
     * Déclenche la terminaison des défis.
     * @return Message de confirmation
     */
    @PutMapping("/terminer-defis")
    public String triggerTerminerDefis() {
        service.terminerDefis();
        return "Défis terminés.";
    }

    /**
     * Déclenche la terminaison des récompenses expirées.
     * @return Message de confirmation
     */
    @PutMapping("/terminer-recompenses")
    public String triggerTerminerRecompenses() {
        service.terminerAttributionsRecompensesExpires();
        return "Récompenses expirées traitées.";
    }

    /**
     * Réinitialise les votes pour les excuses valides.
     * @return Message de confirmation
     */
    @PutMapping("/reinitialiser-votes")
    public String reinitialiserVotesExcusesValide() {
        service.reinitialiserVotesExcusesValide();
        return "Excuses réinitialisées.";
    }
}
