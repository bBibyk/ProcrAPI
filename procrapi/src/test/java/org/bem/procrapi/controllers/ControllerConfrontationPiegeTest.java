package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.ConfrontationPiege;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceConfrontationPiege;
import org.bem.procrapi.utilities.dto.ImportConfrontationPiege;
import org.bem.procrapi.utilities.dto.ImportPiegeDeProductivite;
import org.bem.procrapi.utilities.enumerations.ResultatConfrontationPiege;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerConfrontationPiege.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerConfrontationPiegeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceConfrontationPiege serviceConfrontationPiege;

    // Repos mockés pour éviter les erreurs de dépendances
    @MockitoBean
    private RepositoryRecompense repositoryRecompense;

    @MockitoBean
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @MockitoBean
    private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createConfrontation_ok() throws Exception {
        // DTO d'entrée
        ImportPiegeDeProductivite piege = new ImportPiegeDeProductivite();
        piege.setTitre("Piège du destin");

        ImportConfrontationPiege input = new ImportConfrontationPiege();
        input.setPiege(piege);
        input.setResultat(ResultatConfrontationPiege.SUCCES);

        // Entité attendue
        ConfrontationPiege expected = new ConfrontationPiege();
        expected.setId(42L);

        when(serviceConfrontationPiege.create("Piège du destin", ResultatConfrontationPiege.SUCCES))
                .thenReturn(expected);

        mockMvc.perform(post("/api/confrontationpiege/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(42L));
    }

    @Test
    void createConfrontation_validationError() throws Exception {
        // DTO d'entrée
        ImportPiegeDeProductivite piege = new ImportPiegeDeProductivite();
        piege.setTitre("Piège buggé");

        ImportConfrontationPiege input = new ImportConfrontationPiege();
        input.setPiege(piege);
        input.setResultat(ResultatConfrontationPiege.ECHEC);

        when(serviceConfrontationPiege.create("Piège buggé", ResultatConfrontationPiege.ECHEC))
                .thenThrow(new ServiceValidationException("Résultat invalide"));

        mockMvc.perform(post("/api/confrontationpiege/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Résultat invalide"));
    }
}
