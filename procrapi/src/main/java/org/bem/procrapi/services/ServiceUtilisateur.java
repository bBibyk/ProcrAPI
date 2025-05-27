package org.bem.procrapi.services;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.repositories.RepositoryExcuseCreative;
import org.bem.procrapi.repositories.RepositoryUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ServiceUtilisateur {

    private final RepositoryUtilisateur repositoryUtilisateur;
    private final RepositoryExcuseCreative repositoryExcuseCreative;

    @Autowired
    public ServiceUtilisateur(RepositoryUtilisateur repositoryUtilisateur, RepositoryExcuseCreative repositoryExcuseCreative) {
        this.repositoryUtilisateur = repositoryUtilisateur;
        this.repositoryExcuseCreative = repositoryExcuseCreative;
    }

    public Optional<Utilisateur> createUtilisateur(Utilisateur utilisateur) {
        if (repositoryUtilisateur.findByEmail(utilisateur.getEmail()).isPresent()) {
            return Optional.empty();
        }

        if (utilisateur.getExcusePreferee() != null && utilisateur.getExcusePreferee().getId() != null) {
            Optional<ExcuseCreative> excuse = repositoryExcuseCreative.findById(utilisateur.getExcusePreferee().getId());
            if (excuse.isEmpty()) {
                return Optional.empty();
            }
            utilisateur.setExcusePreferee(excuse.get());
        }

        if (utilisateur.getDateInscription() == null) {
            utilisateur.setDateInscription(new Date());
        }
        if (utilisateur.getPointsAccumules() == null) {
            utilisateur.setPointsAccumules(0);
        }

        Utilisateur savedUser = repositoryUtilisateur.save(utilisateur);
        return Optional.of(savedUser);
    }
}
