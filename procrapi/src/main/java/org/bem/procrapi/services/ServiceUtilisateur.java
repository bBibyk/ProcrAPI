package org.bem.procrapi.services;

import org.bem.procrapi.authentication.EmailHolder;
import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.bem.procrapi.utilities.enumerations.NiveauProcrastination;
import org.bem.procrapi.utilities.enumerations.RoleUtilisateur;
import org.bem.procrapi.utilities.exceptions.ServiceValidationException;
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
    //method accessible seulement pour les services donc protected
    protected Utilisateur getUtilisateurCourant(){
        Optional<Utilisateur> utilisateurCourant = repositoryUtilisateur.findByEmail(EmailHolder.getEmail());
        if(utilisateurCourant.isEmpty()){
            throw new ServiceValidationException("Vous n'êtes pas authentifié.");
        }
        return utilisateurCourant.get();
    }

    public Utilisateur create(RoleUtilisateur role,
                              String pseudo,
                              String email,
                              ExcuseCreative excusePreferee) throws  ServiceValidationException {
        if (role==RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new ServiceValidationException("Cet utilisateur ne peut pas être crée.");
        } else if (role == RoleUtilisateur.ANTIPROCRASTINATEUR_REPENTI
                && getUtilisateurCourant().getRole() != RoleUtilisateur.GESTIONNAIRE_DU_TEMPS_PERDU){
            throw new ServiceValidationException("Seul le Gestionnaire du temps perdu peut créer des antiprocrastinateurs répantis.");
        } else if (role == null){
            throw new ServiceValidationException("Role non valide.");
        } else if (pseudo == null ){
            throw new ServiceValidationException("Pseudo non valide.");
        } else if (repositoryUtilisateur.findByEmail(email).isPresent()) {
            throw new ServiceValidationException("Email non valide.");
        }

        Utilisateur savedUtilisateur = new Utilisateur();
        if (excusePreferee != null){
            Optional<ExcuseCreative> excusePrefereeFull = repositoryExcuseCreative.findById(excusePreferee.getId());
            if (excusePrefereeFull.isEmpty()){
                throw new ServiceValidationException("Excuse non valide.");
            }
            savedUtilisateur.setExcusePreferee(excusePrefereeFull.get());
        }

        savedUtilisateur.setEmail(email);
        savedUtilisateur.setPseudo(pseudo);
        savedUtilisateur.setRole(role);
        return repositoryUtilisateur.save(savedUtilisateur);
    }

    //method accessible seulement pour les services donc protected
    protected void attribuerPoints(Utilisateur utilisateur, Integer points) {
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
