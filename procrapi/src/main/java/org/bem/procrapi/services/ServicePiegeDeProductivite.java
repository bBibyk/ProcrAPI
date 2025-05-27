package org.bem.procrapi.services;

import org.bem.procrapi.entities.PiegeDeProductivite;
import org.bem.procrapi.repositories.RepositoryPiegeDeProductivite;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ServicePiegeDeProductivite {

    private final RepositoryPiegeDeProductivite piegeRepository;

    public ServicePiegeDeProductivite(RepositoryPiegeDeProductivite piegeRepository) {
        this.piegeRepository = piegeRepository;
    }


    public Optional<PiegeDeProductivite> creerPiege(String titre) {
        PiegeDeProductivite piege = new PiegeDeProductivite();
        piege.setTitre(titre);
        piege.setDateCreation(new Date());

        return Optional.of(piegeRepository.save(piege));
    }


    public boolean supprimerPiege(String titre) {
        Optional<PiegeDeProductivite> piegeOpt = piegeRepository.findByTitre(titre);

        if (piegeOpt.isPresent()) {
            piegeRepository.delete(piegeOpt.get());
            return true;
        }
        return false;
    }

    }
