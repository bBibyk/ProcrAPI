package org.bem.procrapi.services;

import lombok.RequiredArgsConstructor;

import org.bem.procrapi.entities.ExcuseCreative;
import org.bem.procrapi.repositories.ExcuseCreativeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcuseCreativeService {
    private final ExcuseCreativeRepository repository;

    public ExcuseCreative creer(String texte) {
        return repository.save(ExcuseCreative.builder().texte(texte).build());
    }

    public void supprimer(Long id) {
        repository.deleteById(id);
    }
}
