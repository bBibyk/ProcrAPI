package org.bem.procrapi.controllers;

import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceVerificationPeriodique;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerVerificationPeriodique.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerVerificationPeriodiqueTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceVerificationPeriodique service;

    // Repos mockés pour éviter les erreurs de dépendances
    @MockitoBean private RepositoryRecompense repositoryRecompense;
    @MockitoBean private RepositoryExcuseCreative repositoryExcuseCreative;
    @MockitoBean private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void triggerReevaluerPointsTachesEvitees_ok() throws Exception {
        doNothing().when(service).reevaluerPointsTachesEvitees();

        mockMvc.perform(put("/api/admin/reevaluer-points-taches-evitees"))
                .andExpect(status().isOk())
                .andExpect(content().string("Réévaluation des points terminée."));
    }

    @Test
    void triggerCommencerDefis_ok() throws Exception {
        doNothing().when(service).commencerDefis();

        mockMvc.perform(put("/api/admin/commencer-defis"))
                .andExpect(status().isOk())
                .andExpect(content().string("Défis commencés."));
    }

    @Test
    void triggerTerminerDefis_ok() throws Exception {
        doNothing().when(service).terminerDefis();

        mockMvc.perform(put("/api/admin/terminer-defis"))
                .andExpect(status().isOk())
                .andExpect(content().string("Défis terminés."));
    }

    @Test
    void triggerTerminerRecompenses_ok() throws Exception {
        doNothing().when(service).terminerAttributionsRecompensesExpires();

        mockMvc.perform(put("/api/admin/terminer-recompenses"))
                .andExpect(status().isOk())
                .andExpect(content().string("Récompenses expirées traitées."));
    }

    @Test
    void reinitialiserVotesExcusesValide_ok() throws Exception {
        doNothing().when(service).reinitialiserVotesExcusesValide();

        mockMvc.perform(put("/api/admin/reinitialiser-votes"))
                .andExpect(status().isOk())
                .andExpect(content().string("Excuses réinitialisées."));
    }
}
