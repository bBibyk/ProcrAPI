package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.TacheAEviter;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceTacheAEviter;
import org.bem.procrapi.utilities.dto.ImportSetStatutTache;
import org.bem.procrapi.utilities.dto.ImportTacheAEviter;
import org.bem.procrapi.utilities.enumerations.StatutTache;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerTacheAEviter.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerTacheAEviterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceTacheAEviter serviceTacheAEviter;

    // Dépendances nécessaires au contexte
    @MockitoBean private RepositoryRecompense repositoryRecompense;
    @MockitoBean private RepositoryExcuseCreative repositoryExcuseCreative;
    @MockitoBean private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createTache_ok() throws Exception {
        ImportTacheAEviter dto = new ImportTacheAEviter();
        dto.setTitre("Tâche inutile");
        dto.setDateLimite(LocalDate.of(2025, 6, 20));
        dto.setDegreUrgence(3);
        dto.setDescription("À éviter absolument");
        dto.setConsequences("Retard assuré");

        TacheAEviter saved = new TacheAEviter();
        saved.setTitre(dto.getTitre());

        when(serviceTacheAEviter.create(
                dto.getDateLimite(),
                dto.getTitre(),
                dto.getDegreUrgence(),
                dto.getConsequences(),
                dto.getDescription()
        )).thenReturn(saved);

        mockMvc.perform(post("/api/tacheaeviter/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre").value("Tâche inutile"));
    }

    @Test
    void createTache_validationError() throws Exception {
        ImportTacheAEviter dto = new ImportTacheAEviter();
        dto.setTitre(null); // champ obligatoire manquant

        when(serviceTacheAEviter.create(
                dto.getDateLimite(),
                dto.getTitre(),
                dto.getDegreUrgence(),
                dto.getConsequences(),
                dto.getDescription()
        )).thenThrow(new ServiceValidationException("Titre obligatoire"));

        mockMvc.perform(post("/api/tacheaeviter/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Titre obligatoire"));
    }

    @Test
    void setStatut_ok() throws Exception {
        ImportSetStatutTache input = new ImportSetStatutTache();
        input.setTitreTache("Tâche inutile");
        input.setStatut(StatutTache.CATASTROPHE);

        TacheAEviter updated = new TacheAEviter();
        updated.setTitre("Tâche inutile");

        when(serviceTacheAEviter.setStatut(input.getTitreTache(), input.getStatut()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/tacheaeviter/set-statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Tâche inutile"));
    }

    @Test
    void setStatut_error() throws Exception {
        ImportSetStatutTache input = new ImportSetStatutTache();
        input.setTitreTache("Tâche inconnue");
        input.setStatut(StatutTache.EVITE_AVEC_SUCCES);

        when(serviceTacheAEviter.setStatut(input.getTitreTache(), input.getStatut()))
                .thenThrow(new ServiceValidationException("Tâche introuvable"));

        mockMvc.perform(put("/api/tacheaeviter/set-statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Tâche introuvable"));
    }
}
