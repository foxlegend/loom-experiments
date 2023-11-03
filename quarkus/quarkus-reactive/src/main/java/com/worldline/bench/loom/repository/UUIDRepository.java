package com.worldline.bench.loom.repository;

import org.hibernate.reactive.mutiny.Mutiny;

import com.worldline.bench.loom.entity.UUID;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UUIDRepository implements PanacheRepository<UUID> {
    @Inject
    Mutiny.SessionFactory sf;

    public Uni<UUID> generateRandomUUID(double sleep) {
        // return find("select floor(random()*10000+1) as id, gen_random_uuid() as uuid, pg_sleep(?1) as sleep from UUID", sleep).firstResult();
        return Panache.getSession().flatMap(session -> {
            return session.<UUID>createNativeQuery("""
                SELECT floor(random()*10000+1) as id, CAST(gen_random_uuid() AS text) as uuid, pg_sleep(:sleep)
                """, UUID.class).setParameter("sleep", sleep).getSingleResult();
            });
    }
}
