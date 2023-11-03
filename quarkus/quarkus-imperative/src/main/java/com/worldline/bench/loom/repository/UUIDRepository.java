package com.worldline.bench.loom.repository;

import com.worldline.bench.loom.entity.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class UUIDRepository {
    
    @Inject
    EntityManager em;

    public UUID generateRandom(double sleep) {
        return (UUID) em.createNativeQuery("""
                SELECT floor(random()*10000+1) as id, gen_random_uuid() as uuid, pg_sleep(:sleep)
                """, UUID.class).setParameter("sleep", sleep).getSingleResult();
    }
}
