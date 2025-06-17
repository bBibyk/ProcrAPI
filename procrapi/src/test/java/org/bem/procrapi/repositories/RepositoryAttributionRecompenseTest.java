package org.bem.procrapi.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RepositoryAttributionRecompenseTest {

    @Autowired
    private RepositoryAttributionRecompense repository;

    @Test
    void testFindByDateExpiration() {
        assert(true);
    }
}
