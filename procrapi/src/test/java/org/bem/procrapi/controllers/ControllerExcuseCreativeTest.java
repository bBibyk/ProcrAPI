package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportExcuseCreative;
import org.bem.procrapi.utilities.dto.ImportSetStatutExcuse;
import org.bem.procrapi.utilities.enumerations.CategorieExcuse;
import org.bem.procrapi.utilities.enumerations.StatutExcuse;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerExcuseCreative.class)
@AutoConfigureMockMvc(addFilters = false)
class ControllerExcuseCreativeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceExcuseCreative serviceExcuseCreative;

    // Repos mockés pour éviter les erreurs de dépendances
    @MockitoBean
    private RepositoryRecompense repositoryRecompense;

    @MockitoBean
    private RepositoryExcuseCreative repositoryExcuseCreative;

    @MockitoBean
    private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createExcuse_ok() throws Exception {
        ImportExcuseCreative input = new ImportExcuseCreative();
        input.setTexte("J'étais coincé dans un ascenseur");
        input.setSituation("Retard travail");
        input.setCategorie(CategorieExcuse.ETUDES);

        ExcuseCreative expected = new ExcuseCreative();
        expected.setId(101L);

        when(serviceExcuseCreative.create(input.getTexte(), input.getSituation(), input.getCategorie()))
                .thenReturn(expected);

        mockMvc.perform(post("/api/excuse/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101L));
    }

    @Test
    void createExcuse_validationError() throws Exception {
        ImportExcuseCreative input = new ImportExcuseCreative();
        input.setTexte("");
        input.setSituation("");
        input.setCategorie(CategorieExcuse.TRAVAIL);

        when(serviceExcuseCreative.create(any(), any(), any()))
                .thenThrow(new ServiceValidationException("Texte invalide"));

        mockMvc.perform(post("/api/excuse/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Texte invalide"));
    }

    @Test
    void getByStatut_ok() throws Exception {
        ImportSetStatutExcuse input = new ImportSetStatutExcuse();
        input.setStatut(StatutExcuse.APPROUVEE);

        ExcuseCreative e1 = new ExcuseCreative();
        e1.setId(11L);
        ExcuseCreative e2 = new ExcuseCreative();
        e2.setId(22L);

        when(serviceExcuseCreative.getExusesByStatut(StatutExcuse.APPROUVEE))
                .thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/api/excuse/get-by-statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(11L))
                .andExpect(jsonPath("$[1].id").value(22L));
    }

    @Test
    void setStatut_ok() throws Exception {
        ImportSetStatutExcuse input = new ImportSetStatutExcuse();
        input.setTexteExcuse("J'étais malade");
        input.setStatut(StatutExcuse.APPROUVEE);

        ExcuseCreative updated = new ExcuseCreative();
        updated.setId(55L);

        when(serviceExcuseCreative.setStatut(input.getTexteExcuse(), input.getStatut()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/excuse/changer-statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(55L));
    }

    @Test
    void setStatut_validationError() throws Exception {
        ImportSetStatutExcuse input = new ImportSetStatutExcuse();
        input.setTexteExcuse("Texte non valide");
        input.setStatut(StatutExcuse.REJETEE);

        when(serviceExcuseCreative.setStatut(input.getTexteExcuse(), input.getStatut()))
                .thenThrow(new ServiceValidationException("Statut invalide"));

        mockMvc.perform(put("/api/excuse/changer-statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Statut invalide"));
    }

    @Test
    void voter_ok() throws Exception {
        ImportExcuseCreative input = new ImportExcuseCreative();
        input.setTexte("Je suis désolé");

        ExcuseCreative voted = new ExcuseCreative();
        voted.setId(77L);

        when(serviceExcuseCreative.voterPourExcuse(input.getTexte()))
                .thenReturn(voted);

        mockMvc.perform(put("/api/excuse/voter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(77L));
    }

    @Test
    void voter_validationError() throws Exception {
        ImportExcuseCreative input = new ImportExcuseCreative();
        input.setTexte("Texte invalide");

        when(serviceExcuseCreative.voterPourExcuse(input.getTexte()))
                .thenThrow(new ServiceValidationException("Impossible de voter"));

        mockMvc.perform(put("/api/excuse/voter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Impossible de voter"));
    }

    @Test
    void classementHebdomadaire_ok() throws Exception {
        ExcuseCreative excuse1 = new ExcuseCreative();
        excuse1.setId(101L);
        ExcuseCreative excuse2 = new ExcuseCreative();
        excuse2.setId(102L);

        when(serviceExcuseCreative.getClassementHebdomadaire())
                .thenReturn(List.of(excuse1, excuse2));

        mockMvc.perform(get("/api/excuse/classement-hebdo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101L))
                .andExpect(jsonPath("$[1].id").value(102L));
    }
}
