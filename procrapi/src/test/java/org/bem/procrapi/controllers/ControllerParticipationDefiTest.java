package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceParticipationDefi;
import org.bem.procrapi.utilities.dto.ImportDefiDeProcrastination;
import org.bem.procrapi.utilities.dto.ImportParticipationDefi;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerParticipationDefi.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerParticipationDefiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceParticipationDefi serviceParticipation;

    // Repos mockés pour éviter erreurs de dépendances
    @MockitoBean
    private RepositoryRecompense repositoryRecompense;

    @MockitoBean
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @MockitoBean
    private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createParticipation_ok() throws Exception {
        ImportParticipationDefi input = new ImportParticipationDefi();
        // simuler un objet defi avec titre
        input.setDefi(new ImportDefiDeProcrastination());
        input.getDefi().setTitre("Defi 123");

        ParticipationDefi saved = new ParticipationDefi();
        saved.setId(1L);

        when(serviceParticipation.create("Defi 123")).thenReturn(saved);

        mockMvc.perform(post("/api/participationdefi/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createParticipation_validationError() throws Exception {
        ImportParticipationDefi input = new ImportParticipationDefi();
        input.setDefi(null);

        when(serviceParticipation.create(null))
                .thenThrow(new ServiceValidationException("Défi obligatoire"));

        mockMvc.perform(post("/api/participationdefi/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Défi obligatoire"));
    }
}
