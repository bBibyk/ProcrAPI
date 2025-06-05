package org.bem.procrapi.repositories;

import org.bem.procrapi.entities.DefiDeProcrastination;
import org.bem.procrapi.entities.ParticipationDefi;
import org.bem.procrapi.entities.Utilisateur;
import org.bem.procrapi.utilities.enumerations.StatutParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryParticipationDefi extends JpaRepository<ParticipationDefi, Long> {

    long countByUtilisateurAndStatutIn(Utilisateur utilisateur, List<StatutParticipation> statuts);

    long countByDefi(DefiDeProcrastination defi);
}
