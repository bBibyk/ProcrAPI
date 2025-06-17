package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceAttributionRecompense;
import org.bem.procrapi.utilities.dto.ImportAttributionRecompense;
import org.bem.procrapi.utilities.dto.ImportRecompense;
import org.bem.procrapi.utilities.dto.ImportUtilisateur;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerAttributionRecompense.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerAttributionRecompenseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ServiceAttributionRecompense serviceAttributionRecompense;

    // Repos mockés pour éviter les erreurs de dépendances
    @MockBean
    private RepositoryRecompense repositoryRecompense;

    @MockBean
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @MockBean
    private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createAttribution_ok() throws Exception {
        // Préparation du DTO d'entrée
        ImportUtilisateur utilisateurDto = new ImportUtilisateur();
        utilisateurDto.setEmail("user@example.com");

        ImportRecompense recompenseDto = new ImportRecompense();
        recompenseDto.setTitre("Médaille d'honneur");

        ImportAttributionRecompense inputDto = new ImportAttributionRecompense();
        inputDto.setUtilisateur(utilisateurDto);
        inputDto.setRecompense(recompenseDto);
        inputDto.setContexteAttribution("Excellence académique");
        inputDto.setDateExpiration(LocalDate.of(2030, 1, 1));

        // Préparation du retour attendu (entité)
        AttributionRecompense expectedEntity = new AttributionRecompense();
        expectedEntity.setId(1L);

        when(serviceAttributionRecompense.create(
                "user@example.com",
                "Médaille d'honneur",
                "Excellence académique",
                LocalDate.of(2030, 1, 1)
        )).thenReturn(expectedEntity);

        // Appel et vérifications
        mockMvc.perform(post("/api/attributions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createAttribution_ko_validation() throws Exception {
        // Préparation d'un DTO incorrect
        ImportUtilisateur utilisateurDto = new ImportUtilisateur();
        utilisateurDto.setEmail("user@example.com");

        ImportRecompense recompenseDto = new ImportRecompense();
        recompenseDto.setTitre("Médaille d'honneur");

        ImportAttributionRecompense inputDto = new ImportAttributionRecompense();
        inputDto.setUtilisateur(utilisateurDto);
        inputDto.setRecompense(recompenseDto);
        inputDto.setContexteAttribution("Invalide");
        inputDto.setDateExpiration(LocalDate.of(2030, 1, 1));

        // Simuler une exception de validation
        when(serviceAttributionRecompense.create(
                "user@example.com",
                "Médaille d'honneur",
                "Invalide",
                LocalDate.of(2030, 1, 1)
        )).thenThrow(new ServiceValidationException("Erreur de validation"));

        mockMvc.perform(post("/api/attributions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur de validation"));
    }
}
