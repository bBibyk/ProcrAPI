package org.bem.procrapi.services;

import org.bem.procrapi.components.authentication.EmailHolder;
import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Initialement ce traitement se faisait dans EmailHolder (qui était UtilisateurHodlder)
    // Mais nous avons opté pour cette solution afin de maintenir le user dans le contexte de persistence
    // De plus ça a permis de factoriser du code pour ne pas refaire le teste à chaque fois si le user
    // n'est pas authentifié
    public Utilisateur getUtilisateurCourant(){
        Optional<Utilisateur> utilisateurCourant = repositoryUtilisateur.findByEmail(EmailHolder.getEmail());
        if(utilisateurCourant.isEmpty()){
            throw new IllegalArgumentException("Vous n'êtes pas authentifié.");
        }
        return utilisateurCourant.get();
    }

    public Utilisateur create(Utilisateur utilisateur) throws  IllegalArgumentException {
        if (utilisateur.getRole()==RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new IllegalArgumentException("Cet utilisateur ne peut pas être crée.");
        } else if (utilisateur.getRole() == RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI
                && getUtilisateurCourant().getRole() != RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new IllegalArgumentException("Seul le Gestionnaire du temps perdu peut créer des antiprocrastinateurs répantis.");
        } else if (utilisateur.getRole() == null){
            throw new IllegalArgumentException("Role non valide.");
        } else if (utilisateur.getPseudo() == null ){
            throw new IllegalArgumentException("Pseudo non valide.");
        } else if (repositoryUtilisateur.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email non valide.");
        }

        Utilisateur savedUtilisateur = new Utilisateur();
        if (utilisateur.getExcusePreferee() != null){
            Optional<ExcuseCreative> excusePreferee = repositoryExcuseCreative.findById(utilisateur.getExcusePreferee().getId());
            if (excusePreferee.isEmpty()){
                throw new IllegalArgumentException("Excuse non valide.");
            }
            savedUtilisateur.setExcusePreferee(excusePreferee.get());
        }

        savedUtilisateur.setEmail(utilisateur.getEmail());
        savedUtilisateur.setPseudo(utilisateur.getPseudo());
        savedUtilisateur.setRole(utilisateur.getRole());
        return repositoryUtilisateur.save(savedUtilisateur);
    }

    public void attribuerPoints(Utilisateur utilisateur, Integer points) {
        int nouveauxPoints = utilisateur.getPointsAccumules() + points;
        if (nouveauxPoints <= 0){
            utilisateur.setPointsAccumules(0);
            repositoryUtilisateur.save(utilisateur);
            return;
        }
        utilisateur.setPointsAccumules(nouveauxPoints);
        if (utilisateur.getNiveau()== NiveauProcrastination.DEBUTANT && nouveauxPoints>=500){
            utilisateur.setNiveau(NiveauProcrastination.INTERMEDIAIRE);
        } else if (utilisateur.getNiveau()== NiveauProcrastination.INTERMEDIAIRE && nouveauxPoints>=1000){
            utilisateur.setNiveau(NiveauProcrastination.EXPERT);
        }
        repositoryUtilisateur.save(utilisateur);
    }
}
