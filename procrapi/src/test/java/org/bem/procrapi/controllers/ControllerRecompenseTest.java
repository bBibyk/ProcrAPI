package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.Recompense;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceRecompense;
import org.bem.procrapi.utilities.dto.ImportRecompense;
import org.bem.procrapi.utilities.enumerations.NiveauDePrestige;
import org.bem.procrapi.utilities.enumerations.TypeRecompense;
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

@WebMvcTest(ControllerRecompense.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerRecompenseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceRecompense serviceRecompense;

    // Dépendances annexes mockées
    @MockitoBean
    private RepositoryRecompense repositoryRecompense;

    @MockitoBean
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @MockitoBean
    private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createRecompense_ok() throws Exception {
        ImportRecompense input = new ImportRecompense();
        input.setTitre("Titre récompense");
        input.setDescription("Une belle récompense");
        input.setConditionsObtention("Terminer 3 tâches");
        input.setNiveau(NiveauDePrestige.OR);
        input.setType(TypeRecompense.POUVOIR_SPECIAL);

        Recompense saved = new Recompense();
        saved.setId(1L);
        saved.setTitre(input.getTitre());

        when(serviceRecompense.create(
                input.getTitre(),
                input.getDescription(),
                input.getConditionsObtention(),
                input.getNiveau(),
                input.getType()
        )).thenReturn(saved);

        mockMvc.perform(post("/api/recompense/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titre").value("Titre récompense"));
    }

    @Test
    void createRecompense_validationError() throws Exception {
        ImportRecompense input = new ImportRecompense();
        input.setTitre(null); // Titre manquant pour provoquer l'erreur

        when(serviceRecompense.create(
                input.getTitre(),
                input.getDescription(),
                input.getConditionsObtention(),
                input.getNiveau(),
                input.getType()
        )).thenThrow(new ServiceValidationException("Le titre est requis"));

        mockMvc.perform(post("/api/recompense/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Le titre est requis"));
    }
}