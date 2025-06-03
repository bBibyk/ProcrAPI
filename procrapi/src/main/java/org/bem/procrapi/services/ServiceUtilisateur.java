package org.bem.procrapi.services;

import org.bem.procrapi.authentication.UtilisateurHolder;
import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class ServiceUtilisateur {

    private final RepositoryUtilisateur repositoryUtilisateur;
    private final RepositoryExcuseCreative repositoryExcuseCreative;

    @Autowired
    public ServiceUtilisateur(RepositoryUtilisateur repositoryUtilisateur,
                              RepositoryExcuseCreative repositoryExcuseCreative) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryExcuseCreative = repositoryExcuseCreative;
    }

    public Optional<Utilisateur> create(Utilisateur utilisateur) throws  IllegalArgumentException {
        if (utilisateur.getRole() == null){
            throw new IllegalArgumentException("Role non valide");
        } else if (utilisateur.getPseudo() == null ){
            throw new IllegalArgumentException("Pseudo non valide");
        } else if (repositoryUtilisateur.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email non valide");
        } else if (UtilisateurHolder.getCurrentUser() == null){
            throw new IllegalArgumentException("Vous n'êtes pas authentifié");
        } else if (utilisateur.getRole() == RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI
                && UtilisateurHolder.getCurrentUser().getRole() != RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new IllegalArgumentException("Vous n'avez pas ce droit");
        }

        Utilisateur savedUtilisateru = new Utilisateur();
        if (utilisateur.getExcusePreferee() != null){
            Optional<ExcuseCreative> excusePreferee = repositoryExcuseCreative.findById(utilisateur.getExcusePreferee().getId());
            if (excusePreferee.isEmpty()){
                throw new IllegalArgumentException("Excuse non valide");
            }
            savedUtilisateru.setExcusePreferee(excusePreferee.get());
        }

        savedUtilisateru.setEmail(utilisateur.getEmail());
        savedUtilisateru.setPseudo(utilisateur.getPseudo());
        savedUtilisateru.setRole(utilisateur.getRole());
        return Optional.of(savedUtilisateru);
    }

    public Boolean ajouterPoints(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
