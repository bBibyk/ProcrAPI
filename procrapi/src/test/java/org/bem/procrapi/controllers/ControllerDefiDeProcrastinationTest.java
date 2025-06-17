package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceDefiDeProcrastination;
import org.bem.procrapi.utilities.dto.ImportDefiDeProcrastination;
import org.bem.procrapi.utilities.enumerations.DifficulteDefi;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerDefiDeProcrastination.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerDefiDeProcrastinationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceDefiDeProcrastination serviceDefiDeProcrastination;

    // Repos mockés pour éviter les erreurs de dépendances
    @MockitoBean
    private RepositoryRecompense repositoryRecompense;

    @MockitoBean
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @MockitoBean
    private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createDefi_ok() throws Exception {
        ImportDefiDeProcrastination input = new ImportDefiDeProcrastination();
        input.setTitre("Finir la thèse");
        input.setDateDebut(LocalDate.of(2025, 6, 17));
        input.setDuree(10);
        input.setPointsAGagner(100);
        input.setDescription("Écrire un chapitre entier sans procrastiner.");
        input.setDifficulte(DifficulteDefi.DIFFICILE);

        DefiDeProcrastination expected = new DefiDeProcrastination();
        expected.setId(123L);

        when(serviceDefiDeProcrastination.create(
                input.getDateDebut(),
                input.getTitre(),
                input.getDuree(),
                input.getPointsAGagner(),
                input.getDescription(),
                input.getDifficulte()
        )).thenReturn(expected);

        mockMvc.perform(post("/api/defideprocrastination/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(123L));
    }

    @Test
    void createDefi_validationError() throws Exception {
        ImportDefiDeProcrastination input = new ImportDefiDeProcrastination();
        input.setTitre("Défi raté");
        input.setDateDebut(LocalDate.now());
        input.setDuree(-1); // durée invalide
        input.setPointsAGagner(0);
        input.setDescription("Ce défi ne passera pas.");
        input.setDifficulte(DifficulteDefi.FACILE);

        when(serviceDefiDeProcrastination.create(
                input.getDateDebut(),
                input.getTitre(),
                input.getDuree(),
                input.getPointsAGagner(),
                input.getDescription(),
                input.getDifficulte()
        )).thenThrow(new ServiceValidationException("Durée invalide"));

        mockMvc.perform(post("/api/defideprocrastination/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Durée invalide"));
    }
}
