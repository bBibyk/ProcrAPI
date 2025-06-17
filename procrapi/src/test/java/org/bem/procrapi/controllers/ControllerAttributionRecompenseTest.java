package org.bem.procrapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bem.procrapi.entities.AttributionRecompense;
import org.bem.procrapi.services.ServiceAttributionRecompense;
import org.bem.procrapi.utilities.dto.ImportAttributionRecompense;
import org.bem.procrapi.utilities.dto.ImportRecompense;
import org.bem.procrapi.utilities.dto.ImportUtilisateur;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControllerAttributionRecompense.class)
class ControllerAttributionRecompenseTest {
    @Test
    void testCreateSuccess() throws Exception {
    }

    @Test
    @DisplayName("POST /api/attributions/create - Erreur validation")
    void testCreateValidationError() throws Exception {
    }
}
