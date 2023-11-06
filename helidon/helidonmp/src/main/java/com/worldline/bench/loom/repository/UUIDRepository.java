package com.worldline.bench.loom.repository;

import com.worldline.bench.loom.entity.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class UUIDRepository {
    
    @PersistenceContext(unitName = "pu")
    EntityManager entityManager;

    public UUID generateRandom(double sleep) {
        return (UUID) entityManager.createNativeQuery("""
                SELECT floor(random()*10000+1) as id, gen_random_uuid() as uuid, pg_sleep(:sleep)
                """, UUID.class).setParameter("sleep", sleep).getSingleResult();
    }
}
