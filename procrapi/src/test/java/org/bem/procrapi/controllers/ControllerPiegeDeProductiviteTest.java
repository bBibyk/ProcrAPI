package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServicePiegeDeProductivite;
import org.bem.procrapi.utilities.dto.ImportPiegeDeProductivite;
import org.bem.procrapi.utilities.dto.ImportRecompense;
import org.bem.procrapi.utilities.enumerations.TypePiege;
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

@WebMvcTest(ControllerPiegeDeProductivite.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerPiegeDeProductiviteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServicePiegeDeProductivite piegeService;

    // Mocks nécessaires pour éviter les erreurs de dépendances
    @MockitoBean
    private RepositoryRecompense repositoryRecompense;

    @MockitoBean
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @MockitoBean
    private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void creerPiege_ok() throws Exception {
        ImportPiegeDeProductivite input = new ImportPiegeDeProductivite();
        input.setTitre("Piège A");
        input.setType(TypePiege.MEDITATION);
        input.setDifficulte(3);
        input.setDescription("Description piège");
        ImportRecompense recompenseDTO = new ImportRecompense();
        recompenseDTO.setTitre("Récompense 1");
        input.setRecompense(recompenseDTO);
        input.setConsequence("Conséquence forte");

        PiegeDeProductivite saved = new PiegeDeProductivite();
        saved.setId(42L);
        saved.setTitre(input.getTitre());

        when(piegeService.create(
                input.getTitre(),
                input.getType(),
                input.getDifficulte(),
                input.getDescription(),
                input.getRecompense().getTitre(),
                input.getConsequence()
        )).thenReturn(saved);

        mockMvc.perform(post("/api/piegedeproductivite/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(42L))
                .andExpect(jsonPath("$.titre").value("Piège A"));
    }

    @Test
    void creerPiege_validationError() throws Exception {
        ImportPiegeDeProductivite input = new ImportPiegeDeProductivite();
        input.setTitre(null); // Donnée invalide volontaire

        when(piegeService.create(
                input.getTitre(),
                input.getType(),
                input.getDifficulte(),
                input.getDescription(),
                null,
                input.getConsequence()
        )).thenThrow(new ServiceValidationException("Titre obligatoire"));

        mockMvc.perform(post("/api/piegedeproductivite/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Titre obligatoire"));
    }
}
