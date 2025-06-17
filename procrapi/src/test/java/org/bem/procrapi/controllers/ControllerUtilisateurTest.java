package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryRecompense;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.services.ServiceUtilisateur;
import org.bem.procrapi.utilities.dto.ImportUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
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

@WebMvcTest(ControllerUtilisateur.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerUtilisateurTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceUtilisateur serviceUtilisateur;

    // Repos mockés pour éviter les erreurs de dépendances
    @MockitoBean private RepositoryRecompense repositoryRecompense;
    @MockitoBean private RepositoryExcuseCreative repositoryExcuseCreative;
    @MockitoBean private RepositoryUtilisateur repositoryUtilisateur;

    @Test
    void createUtilisateur_ok() throws Exception {
        ImportUtilisateur dto = new ImportUtilisateur();
        dto.setRole(RoleUtilisateur.PROCRASTINATEUR_EN_HERBE);
        dto.setPseudo("Toto");
        dto.setEmail("toto@mail.com");

        Utilisateur saved = new Utilisateur();
        saved.setPseudo("Toto");

        when(serviceUtilisateur.create(dto.getRole(), dto.getPseudo(), dto.getEmail(), null))
                .thenReturn(saved);

        mockMvc.perform(post("/api/utilisateur/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pseudo").value("Toto"));
    }

    @Test
    void createUtilisateur_erreurValidation() throws Exception {
        ImportUtilisateur dto = new ImportUtilisateur();
        dto.setRole(RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI);
        dto.setPseudo(""); // pseudo vide = invalide
        dto.setEmail("invalide@mail.com");

        when(serviceUtilisateur.create(dto.getRole(), dto.getPseudo(), dto.getEmail(), null))
                .thenThrow(new ServiceValidationException("Pseudo invalide"));

        mockMvc.perform(post("/api/utilisateur/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Pseudo invalide"));
    }
}
