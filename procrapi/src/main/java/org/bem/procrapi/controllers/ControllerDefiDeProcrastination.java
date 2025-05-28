package org.bem.procrapi.controllers;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.services.ServiceDefiDeProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path="/api/defideprocrastination")
public class ControllerDefiDeProcrastination {
    private ServiceDefiDeProcrastination serviceDefiDeProcrastination;

    @Autowired
    public ControllerDefiDeProcrastination(ServiceDefiDeProcrastination serviceDefiDeProcrastination) {
        this.serviceDefiDeProcrastination = serviceDefiDeProcrastination;
    }

    @PostMapping(path="/create")
    public ResponseEntity<?> create(@RequestBody DefiDeProcrastination defiDeProcrastination) {
        if(UtilisateurHolder.getCurrentUser()==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentifiez-vous avant d'utiliser cette méthode.");
        }
        if(UtilisateurHolder.getCurrentUser().getRole()!=RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à utiliser cette méthode.");
        }
        Optional<DefiDeProcrastination> createdDefi = serviceDefiDeProcrastination.create(defiDeProcrastination);
        if(createdDefi.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDefi.get());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ce defi ne peut pas être crée.");
    }
}
